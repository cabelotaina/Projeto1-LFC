package desimone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import expressao_regular.ControleER;

public final class SubArvore {
	private static SubArvore INSTANCE;
	private static HashMap<Character, Integer>
		precedence = new HashMap<>();
	
	private SubArvore(){
		precedence.put('*', 1);
		precedence.put('?', 1);
		precedence.put('.', 2);
		precedence.put('|', 3);		
	}
	
	public static synchronized SubArvore getInstance(){
		if(INSTANCE == null)
			INSTANCE = new SubArvore();
		
		return INSTANCE;
	}
	
	/**
	 * Função responsavel para achar a posição
	 * atual da raiz da arvore da ER dada.
	 * 
	 * @param regEx		ER base.
	 * @return			Posição na raiz na String da ER.
	 */
	public int positionOfRoot(String regEx){
		ArrayList<Operator> operators = new ArrayList<>();
		
		getListOfOperators(regEx, operators);
		
		if(operators.size() == 0)
			return -1;
		
		return getPositionLowerPrecedence(operators);
	}
	
	/**
	 * Adiciona a lista dada os operadores que podem se tornar
	 * raiz da arvore da ER dada.
	 * 
	 * @param regEx			ER base.
	 * @param operators		Lista de base para receber os operadores. 
	 */
	private void getListOfOperators(String regEx, ArrayList<Operator> operators) {
		Stack<Character> stackParentheses = new Stack<>();
		char tmp;
		for(int i = 0; i<regEx.length(); i++){
			tmp = regEx.charAt(i);
			if(stackParentheses.isEmpty() && ControleER.isOperator(tmp,false))
				operators.add(new Operator(tmp, i));
			else if(tmp == '(')
				stackParentheses.push('(');
			else if(tmp == ')')
				stackParentheses.pop();
		}
	}
	
	/**
	 * Retorna a posição do operador com menor precedencia na lista
	 * de operadores dada.
	 * 
	 * @param operators		Lista de operadores base.
	 * @return				Posição do operador com menor precedencia.
	 */
	private int getPositionLowerPrecedence(ArrayList<Operator> operators) {
		int lower = 0;
		for (int i = 0; i < operators.size(); i++) {
			if(lowerPrecedence(operators.get(lower).simbol, operators.get(i).simbol))
				lower = i;
		}
		return operators.get(lower).position;
	}
	
	private boolean lowerPrecedence(char c1, char c2){
		return precedence.get(c1) < precedence.get(c2);
	}
	
	/**
	 * Classe interna usada em algumas operações.
	 */
	private class Operator{
		public char simbol;
		public int position;
		
		public Operator(char simbol, int position){
			this.simbol = simbol;
			this.position = position;
		}
	}
}
