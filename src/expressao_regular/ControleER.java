package expressao_regular;

import automato.Automato;
import desimone.DeSimone;

public class ControleER {
	
	public static final String simbolosPermitidos = 
			"abcdefghijklmnopqrstuvwxyz0123456789&";
	public static final String operadoresPermitidos = 
			"()*+.|?";
	
	public static ExpressaoRegular criarExpressaoRegular(String titulo, String expressao_regular){
		expressao_regular = formatarExpressaoRegular(expressao_regular);
		if(expressao_regular == null) return null;
		return new ExpressaoRegular(expressao_regular, titulo);
	}
	
	public static Automato criarAutomato(ExpressaoRegular expressao_regular){
		Automato automato = DeSimone.criarAutomato(expressao_regular.obterExpressaoRegular());
		automato.titulo(expressao_regular.titulo());
		automato.extras("AF");
		automato.ordernarAlfabeto();
		return automato;
	}
	
	private static String formatarExpressaoRegular(String expressao_regular){
		expressao_regular = expressao_regular.replaceAll("\\s*", "");

		if(ehUmaExpressaoRegularValida(expressao_regular)){
			expressao_regular = substituirOperadoresComplexos(expressao_regular); 
			expressao_regular = explicitarConcatenacao(expressao_regular);
			return expressao_regular;
		}else
			return null;
	}
	
	private static boolean ehUmaExpressaoRegularValida(String expressao_regular){
		int quantidadeParenteses = 0;
	
		if(expressao_regular.contains("()"))
			return false;
		
		for(int i = 0; i < expressao_regular.length(); i++){
			char c = expressao_regular.charAt(i);
			if(ehOperador(c,true)){
				if(c == '(')
					++quantidadeParenteses;
				else if(c == ')')
					--quantidadeParenteses;
				else if(ehOperadorUnario(c)){
					if(i == 0) 
						return false;
					char c1 = expressao_regular.charAt(i-1);
					if(ehOperador(c1,true) && c1 != ')') 
						return false;
				}else if(ehOperadorBinario(c)){
					if(i == 0 || i >= expressao_regular.length()) 
						return false;
					char c1 = expressao_regular.charAt(i-1);
					char c2 = expressao_regular.charAt(i+1);
					if((c1 == '(' || ehOperadorBinario(c1)) || (ehOperador(c2,true) && c2 != '('))
						return false;
				}
			}else if(simbolosPermitidos.indexOf(c) == -1)
				return false;
		}
		return quantidadeParenteses == 0;
	}
	
	private static String explicitarConcatenacao(String expressao_regular){
		String tmp = "";
		
		for(int i = 0; i < expressao_regular.length(); i++){
			char c = expressao_regular.charAt(i);
			if(!ehOperador(c,true)){
				if(i > 0 && (expressao_regular.charAt(i-1) == ')' || ehOperadorUnario(expressao_regular.charAt(i-1))))
					tmp += '.';
				if(i < expressao_regular.length()-1 && (!ehOperador(expressao_regular.charAt(i+1),true) || expressao_regular.charAt(i+1) == '('))
					tmp += (c+""+'.');
				else
					tmp += c;
			}else if(i > 0 && c == '(' && (expressao_regular.charAt(i-1) == ')' || ehOperadorUnario(expressao_regular.charAt(i-1)) ))
				tmp += ('.'+""+c);
			else
				tmp += c;
		}
		return tmp;
	}
	
	private static String substituirOperadoresComplexos(String regEx){
		regEx = regEx.replaceAll("(\\w)\\+", "$1$1\\*");//retira operador '+'
		regEx = regEx.replaceAll("(\\([a-z0-9\\.\\|\\*\\?]+\\))\\+", "$1$1\\*");//retira operador '+'
		return regEx;
	}
	
	public static boolean ehOperador(char c, boolean comParenteses){
		if(!comParenteses)
			return operadoresPermitidos.substring(2, operadoresPermitidos.length()).indexOf(c) != -1;
		else
			return operadoresPermitidos.indexOf(c) != -1;
	}
	
	public static boolean ehOperadorBinario(char c){
		return (c=='|' || c=='.');
	}
	
	public static boolean ehOperadorUnario(char c){
		return (c=='?' || c=='+' || c=='*');
	}
}
