package gramatica;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import automato.Automato;
import automato.Estado;

public abstract class ControleGR {
	
	public static final String allowedSimbols = 
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789&";
	public static final String allowedExtraSimbols =
			"->|";
	private static final String pattern =
			"([A-Z][0-9]?->(([a-z0-9]([A-Z][0-9]?)|[a-z0-9&])(\\|))*([a-z0-9]([A-Z][0-9]?)(?![0-9]?->)|[a-z0-9&]))";
	
	public static Gramatica createRegGrammar(String titulo, String grammar){
		grammar = grammar.replaceAll("\\s*", "");
		
		if(!isValidRegGrammar(grammar))
			return null;
		
		//String newLine = System.getProperty("line.separator");
		
		Gramatica G = new Gramatica(titulo);
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(grammar);
		
		grammar = "";
		
		String linha, vn, tmpVt, tmpVn;
		String[] split;
		while(matcher.find()){
			linha = matcher.group();
			grammar += linha+"\n";
			
			split = linha.split("->");
			vn = split[0]; 
			G.addVn(vn);
			if(G.getInitialSimbol() == null)
				G.setInitialSimbol(vn);
			
			split = split[1].split("\\|");
			for(String elements : split){
				if(elements.length() > 0){
					tmpVt = elements.substring(0, 1);
					if(elements.length() > 1)
						tmpVn = elements.substring(1, elements.length());
					else
						tmpVn = "$";
					
					G.addVt(tmpVt.charAt(0));
					G.addProduction(vn, tmpVt.charAt(0), tmpVn);
				}
			}
		}
		G.setGrammar(grammar);
		
		return G;
	}
	
	private static boolean isValidRegGrammar(String grammar){
		//return grammar.matches("([A-Z][0-9]?->(([a-z0-9][A-Z]?(?!->)|[a-z0-9&])(\\|)?)+)+");
		return grammar.matches(pattern+"+");
	}
	
	public static Automato criarAutomato(Gramatica grammar){
		
		Automato af = new Automato(grammar.getVt());
		af.titulo(grammar.titulo());
		grammar.extra("AF");
		
		Estado sFinal = new Estado("FINAL");
		af.adicionarEstado(sFinal);
		af.estadoFinal(sFinal);

		for(String vn : grammar.getVn()){
			Estado s = new Estado(vn);
			af.adicionarEstado(s);
			if(vn.equals(grammar.getInitialSimbol()))
				af.estadoInicial(s);
			
			for(Producao p : grammar.getProductions(vn)){
				if(p.getNext().equals("$"))
					af.adicionarTransicao(s, p.getGenerated(), sFinal);
				else
					af.adicionarTransicao(s, p.getGenerated(), new Estado(p.getNext()));
				
				if(vn.equals(grammar.getInitialSimbol()) && p.getGenerated() == '&')
					af.estadoFinal(s);
			}
		}
		af.sortStates();
		af.ordernarAlfabeto();
		
		return af;
	}
}
