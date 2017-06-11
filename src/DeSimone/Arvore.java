package DeSimone;

import java.util.ArrayList;
import java.util.Stack;

public class Arvore {
	private No raiz;
	private ArrayList<No> listaDeFolhas;
	public static final String operadoresPermitidos = "()*+.|?";

	public Arvore(String expressao_regular) {
		raiz = criarSubArvores(null, expressao_regular);
		listaDeFolhas = new ArrayList<>();
		adicionarFolhasEmOrdem();
		costurarEmOrdem(raiz);
	}

	public void adicionarFolhasEmOrdem(){
	  Stack<No> pilhaDeNos = new Stack<>();
	  No no = this.raiz;
	  int numeroFolha = 1;
	  
	  while (!pilhaDeNos.isEmpty() || no != null){
		  if (no != null){
			  pilhaDeNos.push(no);
			  no = no.esquerda();
		  }
		  else {
			  no = pilhaDeNos.pop();
			  if (!Arvore.operador(no.simbolo(),false)){
				  no.numeroFolha(numeroFolha);
				  this.listaDeFolhas.add(no);
				  numeroFolha++;
			  }
			  no = no.direita();
		  }
	  }
  }

	public static boolean operador(char c, boolean comParenteses) {
		if (!comParenteses) {
			return operadoresPermitidos.substring(2, operadoresPermitidos.length()).indexOf(c) != -1;
		}
		return operadoresPermitidos.indexOf(c) != -1;
	}

	public void costurarEmOrdem(No no) {
		if (no == null) {
			return;
		}

		costurarEmOrdem(no.esquerda());

		if (!Arvore.binario(no.simbolo())) {
			No raiz = no.raiz();
			if (raiz != null) {
				while (raiz.costurado()) {
					raiz = raiz.raiz();
					if (raiz == null) {
						no.costura(new No('$', null));
						return;
					}
				}
			} else {
				no.costura(new No('$', null));
			}
		}
		costurarEmOrdem(no.direita());
	}

	public static boolean binario(char operador) {
		return (operador == '|' || operador == '.');
	}

	public No obterRaiz() {
		return this.raiz;
	}

	public ArrayList<No> obterFolhas() {
		return this.listaDeFolhas;
	}

	public No criarSubArvores(No no, String expressao_regular) {
		No novoNo = new No();
		SubArvore subArvore = SubArvore.obterInstancia();

		expressao_regular = removerParenteses(expressao_regular);
		int raiz = subArvore.posicaoDaRaiz(expressao_regular);
		if (raiz == -1 && expressao_regular.length() > 1) {
			return criarSubArvores(no, expressao_regular);
		} else if (raiz == -1 && expressao_regular.length() == 1) {
			return new No(expressao_regular.charAt(0), no);
		}

		novoNo.nome(expressao_regular.charAt(raiz));
		novoNo.raiz(no);

		String esquerda = expressao_regular.substring(0, raiz);
		String direita = expressao_regular.substring(raiz + 1, expressao_regular.length());

		if (esquerda.length() > 1) {
			novoNo.esquerda(this.criarSubArvores(novoNo, esquerda));
		} else if (esquerda.length() == 1) {
			novoNo.esquerda(new No(esquerda.charAt(0), novoNo));
		}

		if (direita.length() > 1) {
			novoNo.direita(criarSubArvores(novoNo, direita));
		} else if (direita.length() == 1) {
			novoNo.direita(new No(direita.charAt(0), novoNo));
		}
		return novoNo;
	}

	public String removerParenteses(String expressao_regular) {
		String copia_er = expressao_regular;
		Stack<Character> pilhaParenteses = new Stack<>();
		char simbolo;

		if (copia_er.charAt(0) == '(' && copia_er.charAt(expressao_regular.length() - 1) == ')') {
			copia_er = "[" + copia_er.substring((1), copia_er.length() - 1) + "]";
			for (int posicao = 0; posicao < copia_er.length(); posicao++) {
				simbolo = copia_er.charAt(posicao);
				if (simbolo == '[') {
					pilhaParenteses.push('[');
				} else if (simbolo == '(' && pilhaParenteses.peek() == '[') {
					pilhaParenteses.pop();
				} else if (simbolo == '(' && (pilhaParenteses.peek() == '(' || pilhaParenteses.peek() == '[')) {
					pilhaParenteses.push('(');
				} else if (simbolo == ')' && pilhaParenteses.peek() == '(') {
					pilhaParenteses.pop();
				}
			}

			if (pilhaParenteses.isEmpty()) {
				return copia_er.substring(1, copia_er.length() - 1);
			} else {
				return expressao_regular;
			}
		} else {
			return expressao_regular;
		}
	}
}
