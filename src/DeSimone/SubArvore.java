package DeSimone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class SubArvore {
	private static SubArvore INSTANCIA;
	private static HashMap<Character, Integer> precedencia = new HashMap<>();
	public static final String operadoresPermitidos = "()*+.|?";
	private SubArvore() {
		precedencia.put('*', 1);
		precedencia.put('?', 1);
		precedencia.put('.', 2);
		precedencia.put('|', 3);
	}

	public static synchronized SubArvore obterInstancia() {
		if (INSTANCIA == null) {
			INSTANCIA = new SubArvore();
		}
		return INSTANCIA;
	}

	public int posicaoDaRaiz(String expressao_regular) {
		ArrayList<Operador> operadores = new ArrayList<>();

		obterListaDeOperadores(expressao_regular, operadores);

		if (operadores.size() == 0) {
			return -1;
		}

		return obterPosicaoOperadorMenorPrecedencia(operadores);
	}

	private int obterPosicaoOperadorMenorPrecedencia(ArrayList<Operador> operadores ) {
		int candidato = 0;
		for (int posicao = 0; posicao < operadores.size(); posicao++){
			if (precedencia.get(operadores.get(candidato).simbolo) < precedencia.get(operadores.get(posicao).simbolo)){
				candidato = posicao;
			}
		}
		return operadores.get(candidato).posicao;
	}

	public class Operador {
		public char simbolo;
		public int posicao;

		public Operador(char simbolo, int posicao) {
			this.simbolo = simbolo;
			this.posicao = posicao;
		}
	}

	private void obterListaDeOperadores(String expressao_regular, ArrayList<Operador> operadores){
	Stack<Character> pilhaParenteses = new Stack<>();
	
	char operador;
	for (int posicao = 0; posicao < expressao_regular.length(); posicao++){
		operador = expressao_regular.charAt(posicao);
		if (pilhaParenteses.isEmpty() && SubArvore.operador(operador,false)){
			operadores.add(new Operador(operador, posicao));
		}
		else if (operador == '('){
			pilhaParenteses.push('(');
		}
		else if (operador == ')'){
			pilhaParenteses.pop();
		}
	}
  }

  public static boolean operador(char c, boolean withParentheses) {
    if (!withParentheses){
    	return operadoresPermitidos.substring(2, operadoresPermitidos.length()).indexOf(c) != -1;	
    }
	return operadoresPermitidos.indexOf(c) != -1;
  }
}
