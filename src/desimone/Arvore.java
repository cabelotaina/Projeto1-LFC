package desimone;

import java.util.ArrayList;
import java.util.Stack;

import expressao_regular.ControleER;

public class Arvore {
	private No raiz;
	private ArrayList<No> listaFolhas;
	
	public Arvore(String expressao_regular) {
		raiz = createSubTrees(null, expressao_regular);
		listaFolhas = new ArrayList<>();
		adicionarFolhasEmOrdem();
		costuraEmOrdemRec(raiz);
		
		//System.out.println(listaFolhas);
		//imprimirPreOrdemRec(raiz);
	}
	
	public No getRoot(){
		return this.raiz;
	}
	
	public ArrayList<No> getListLeaves(){
		return this.listaFolhas;
	}

	private No createSubTrees(No n, String expressao_regular) {
		No newNodo = new No();
		SubArvore sub = SubArvore.obterInstancia();
		
		expressao_regular = removeExternalParentheses(expressao_regular);
		int raiz = sub.posicaoDaRaiz(expressao_regular);
		
		if(raiz == -1 && expressao_regular.length() > 1)
			return createSubTrees(n, expressao_regular);
		else if(raiz == -1 && expressao_regular.length() == 1)
			return new No(expressao_regular.charAt(0), n);
		
		newNodo.setC(expressao_regular.charAt(raiz));
		newNodo.setPai(n);
		
		String erEsq = expressao_regular.substring(0, raiz);
		String erDir = expressao_regular.substring(raiz+1, expressao_regular.length());
		
		if(erEsq.length() > 1)
			newNodo.setFilhoEsq(createSubTrees(newNodo, erEsq));
		else if(erEsq.length() == 1)
			newNodo.setFilhoEsq(new No(erEsq.charAt(0), newNodo));
		
		if(erDir.length() > 1)
			newNodo.setFilhoDir(createSubTrees(newNodo, erDir));
		else if(erDir.length() == 1)
			newNodo.setFilhoDir(new No(erDir.charAt(0), newNodo));
		
		return newNodo;
	}
	
	private String removeExternalParentheses(String expressao_regular){
		String tmp = expressao_regular;
		Stack<Character> stackParentheses = new Stack<>();
		char cTmp;
		
		if(tmp.charAt(0) == '(' && tmp.charAt(expressao_regular.length()-1) == ')'){
			if(tmp.charAt(0) == '(' && tmp.charAt(tmp.length()-1) == ')')
				tmp = "[" +tmp.substring((1), tmp.length()-1)+ "]";
				
			for (int i = 0; i < tmp.length(); i++) {
				cTmp = tmp.charAt(i);
				if(cTmp == '[')
					stackParentheses.push('[');
				else if(cTmp == ']' && stackParentheses.peek() == '[')
					stackParentheses.pop();
				else if(cTmp == '(' && 
						(stackParentheses.peek() == '(' || 
						stackParentheses.peek() == '['))
					stackParentheses.push('(');
				else if(cTmp == ')' && stackParentheses.peek() == '(')
					stackParentheses.pop();
			}
			
			if(stackParentheses.isEmpty())
				return tmp.substring(1, tmp.length()-1);
			else
				return expressao_regular;
		}else
			return expressao_regular;
	}
	
	private void costuraEmOrdemRec(No root){
		if(root == null)
			return;
		
		costuraEmOrdemRec(root.getFilhoEsq());
		
		if(!ControleER.ehOperadorBinario(root.getC())){
			No pai = root.getPai();
			if(pai != null){
				while(pai.isCosturado()){
					pai = pai.getPai();
					if(pai == null){
						root.setCostura(new No('$', null));//node lambda
						return;
					}
				}
				root.setCostura(pai);
				pai.setIsCosturado(true);
			}else
				root.setCostura(new No('$', null));
		}
		
		costuraEmOrdemRec(root.getFilhoDir());
	}
	
	private void adicionarFolhasEmOrdem(){
		Stack<No> stackNodes = new Stack<>();
		No n = raiz;
		int num = 1;

		while(!stackNodes.isEmpty() || n != null){
			if(n != null){
				stackNodes.push(n);
				n = n.getFilhoEsq();
			}else{
				n = stackNodes.pop();
				if(!ControleER.ehOperador(n.getC(),false)){
					n.setNumero(num);
					listaFolhas.add(n);
					++num;
				}
				n = n.getFilhoDir();
			}
		}
		
	}
	
	public void imprimirPreOrdemRec(No root){
		if(root != null){
			System.out.println("E: "+root.getFilhoEsq()+" - R: "+root+" - D: "+root.getFilhoDir()+" - Cost: "+root.getCostura());
			imprimirPreOrdemRec(root.getFilhoEsq());
			imprimirPreOrdemRec(root.getFilhoDir());
		}
	}
}
