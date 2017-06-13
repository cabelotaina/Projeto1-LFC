package DeSimone;

import java.util.ArrayList;
import java.util.Stack;

public class Arvore {
	private No root;
	private ArrayList<No> listLeaves;
	public static final String allowedOps = 
			"()*+.|?";
	
	public Arvore(String regEx) {
		root = createSubArvores(null, regEx);
		listLeaves = new ArrayList<>();
		addLeavesInOrder();
		costuraEmOrderRec(root);
		
		System.out.println(listLeaves);
		printPreOrderRec(root);
	}
	
	public No getRoot(){
		return this.root;
	}
	
	public ArrayList<No> getListLeaves(){
		return this.listLeaves;
	}

	private No createSubArvores(No n, String regEx) {
		No newNodo = new No();
		SubArvore sub = SubArvore.obterInstancia();
		
		regEx = removeExternalParentheses(regEx);
		int raiz = sub.posicaoDaRaiz(regEx);
		
		if(raiz == -1 && regEx.length() > 1)
			return createSubArvores(n, regEx);
		else if(raiz == -1 && regEx.length() == 1)
			return new No(regEx.charAt(0), n);
		
		newNodo.simbolo(regEx.charAt(raiz));
		newNodo.raiz(n);
		
		String erEsq = regEx.substring(0, raiz);
		String erDir = regEx.substring(raiz+1, regEx.length());
		
		if(erEsq.length() > 1)
			newNodo.esquerda(createSubArvores(newNodo, erEsq));
		else if(erEsq.length() == 1)
			newNodo.esquerda(new No(erEsq.charAt(0), newNodo));
		
		if(erDir.length() > 1)
			newNodo.direita(createSubArvores(newNodo, erDir));
		else if(erDir.length() == 1)
			newNodo.direita(new No(erDir.charAt(0), newNodo));
		
		return newNodo;
	}
	
	private String removeExternalParentheses(String regEx){
		String tmp = regEx;
		Stack<Character> stackParentheses = new Stack<>();
		char cTmp;
		
		if(tmp.charAt(0) == '(' && tmp.charAt(regEx.length()-1) == ')'){
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
				return regEx;
		}else
			return regEx;
	}
	
	private void costuraEmOrderRec(No root){
		if(root == null)
			return;
		
		costuraEmOrderRec(root.esquerda());
		
		if(!isBinaryOperator(root.simbolo())){
			No pai = root.raiz();
			if(pai != null){
				while(pai.costurado()){
					pai = pai.raiz();
					if(pai == null){
						root.costura(new No('$', null));//No lambda
						return;
					}
				}
				root.costura(pai);
				pai.setIsCosturado(true);
			}else
				root.costura(new No('$', null));
		}
		
		costuraEmOrderRec(root.direita());
	}
	
	private void addLeavesInOrder(){
		Stack<No> stackNos = new Stack<>();
		No n = root;
		int num = 1;

		while(!stackNos.isEmpty() || n != null){
			if(n != null){
				stackNos.push(n);
				n = n.esquerda();
			}else{
				n = stackNos.pop();
				if(!isOperator(n.simbolo(),false)){
					n.numeroFolha(num);
					listLeaves.add(n);
					++num;
				}
				n = n.direita();
			}
		}
		
	}
	
	public void printPreOrderRec(No root){
		if(root != null){
			System.out.println("E: "+root.esquerda()+" - R: "+root+" - D: "+root.direita()+" - Cost: "+root.costura());
			printPreOrderRec(root.esquerda());
			printPreOrderRec(root.direita());
		}
	}
	
	public static boolean isOperator(char c, boolean withParentheses){
		if(!withParentheses)
			return allowedOps.substring(2, allowedOps.length()).indexOf(c) != -1;
		else
			return allowedOps.indexOf(c) != -1;
	}
	
	public static boolean isBinaryOperator(char c){
		return (c=='|' || c=='.');
	}
	
}
