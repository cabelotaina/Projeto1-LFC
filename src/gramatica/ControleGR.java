package gramatica;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import automato.Automato;
import automato.Estado;

public abstract class ControleGR {
	
	public static final String simbolosPermitidos = 
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789&";
	public static final String simbolosExtrasPermitidos =
			"->|";
	private static final String padrao =
			"([A-Z][0-9]?->(([a-z0-9]([A-Z][0-9]?)|[a-z0-9&])(\\|))*([a-z0-9]([A-Z][0-9]?)(?![0-9]?->)|[a-z0-9&]))";
	
	public static Gramatica definirGramatica(String titulo, String string_gramatica){
		string_gramatica = string_gramatica.replaceAll("\\s*", "");
		
		if(!ehUmagramatica(string_gramatica))
			return null;
				
		Gramatica gramatica = new Gramatica(titulo);
		Pattern p = Pattern.compile(padrao);
		Matcher matcher = p.matcher(string_gramatica);
		
		string_gramatica = "";
		
		String linha, vn, tmpVt, tmpVn;
		String[] formas_sentenciais;
		while(matcher.find()){
			linha = matcher.group();
			string_gramatica += linha+"\n";
			
			formas_sentenciais = linha.split("->");
			vn = formas_sentenciais[0]; 
			gramatica.adicionarVn(vn);
			if(gramatica.obterSimboloInicial() == null)
				gramatica.definirSimboloInicial(vn);
			
			formas_sentenciais = formas_sentenciais[1].split("\\|");
			for(String forma_sentencial : formas_sentenciais){
				if(forma_sentencial.length() > 0){
					tmpVt = forma_sentencial.substring(0, 1);
					if(forma_sentencial.length() > 1)
						tmpVn = forma_sentencial.substring(1, forma_sentencial.length());
					else
						tmpVn = "$";
					
					gramatica.adicionarVt(tmpVt.charAt(0));
					gramatica.adicionarProducao(vn, tmpVt.charAt(0), tmpVn);
				}
			}
		}
		gramatica.definirGramatica(string_gramatica);
		
		return gramatica;
	}
	
	// Teste para saber se a gramatica é válida
	private static boolean ehUmagramatica(String gramatica){
		return gramatica.matches(padrao+"+");
	}
	
	public static Automato criarAutomato(Gramatica gramatica){
		
		Automato af = new Automato(gramatica.obterVt());
		af.titulo(gramatica.titulo());
		gramatica.extra("AF");
		
		Estado estado_final = new Estado("FINAL");
		af.adicionarEstado(estado_final);
		af.adicionarEstadoFinal(estado_final);

		for(String vn : gramatica.obterVn()){
			Estado s = new Estado(vn);
			af.adicionarEstado(s);
			if(vn.equals(gramatica.obterSimboloInicial()))
				af.adicionarEstadoInicial(s);
			
			for(Producao p : gramatica.obterProducoes(vn)){
				if(p.obterProximo().equals("$"))
					af.adicionarTransicao(s, p.obterSimbolo(), estado_final);
				else
					af.adicionarTransicao(s, p.obterSimbolo(), new Estado(p.obterProximo()));
				
				if(vn.equals(gramatica.obterSimboloInicial()) && p.obterSimbolo() == '&')
					af.adicionarEstadoFinal(s);
			}
		}
		af.ordenarEstados();
		af.ordernarAlfabeto();
		
		return af;
	}
}
