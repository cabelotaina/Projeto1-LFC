package desimone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import expressao_regular.ControleER;

public final class SubArvore {
	private static SubArvore INSTANCIA;
	private static HashMap<Character, Integer>
		precedencia = new HashMap<>();
	
	private SubArvore(){
		precedencia.put('*', 1);
		precedencia.put('?', 1);
		precedencia.put('.', 2);
		precedencia.put('|', 3);		
	}
	
	public static synchronized SubArvore obterInstancia(){
		if(INSTANCIA == null)
			INSTANCIA = new SubArvore();
		
		return INSTANCIA;
	}
	
	/**
	 * Função responsavel para achar a posição
	 * atual da raiz da arvore da ER dada.
	 * 
	 * @param expressao_regular		ER base.
	 * @return			Posição na raiz na String da ER.
	 */
	public int posicaoDaRaiz(String expressao_regular){
		ArrayList<Operador> operadores = new ArrayList<>();
		
		obterListaDeOperadores(expressao_regular, operadores);
		
		if(operadores.size() == 0)
			return -1;
		
		return obterPosicaoMenorPrecedencia(operadores);
	}
	
	/**
	 * Adiciona a lista dada os operadores que podem se tornar
	 * raiz da arvore da ER dada.
	 * 
	 * @param expressao_regular			ER base.
	 * @param operadores		Lista de base para receber os operadores. 
	 */
	private void obterListaDeOperadores(String expressao_regular, ArrayList<Operador> operadores) {
		Stack<Character> pilhaParenteses = new Stack<>();
		char tmp;
		for(int i = 0; i<expressao_regular.length(); i++){
			tmp = expressao_regular.charAt(i);
			if(pilhaParenteses.isEmpty() && ControleER.ehOperador(tmp,false))
				operadores.add(new Operador(tmp, i));
			else if(tmp == '(')
				pilhaParenteses.push('(');
			else if(tmp == ')')
				pilhaParenteses.pop();
		}
	}
	
	/**
	 * Retorna a posição do operador com menor precedencia na lista
	 * de operadores dada.
	 * 
	 * @param operadores		Lista de operadores base.
	 * @return				Posição do operador com menor precedencia.
	 */
	private int obterPosicaoMenorPrecedencia(ArrayList<Operador> operadores) {
		int lower = 0;
		for (int i = 0; i < operadores.size(); i++) {
			if(menorPrecedencia(operadores.get(lower).simbol, operadores.get(i).simbol))
				lower = i;
		}
		return operadores.get(lower).position;
	}
	
	private boolean menorPrecedencia(char c1, char c2){
		return precedencia.get(c1) < precedencia.get(c2);
	}
	
	/**
	 * Classe interna usada em algumas operações.
	 */
	private class Operador{
		public char simbol;
		public int position;
		
		public Operador(char simbol, int position){
			this.simbol = simbol;
			this.position = position;
		}
	}
}
