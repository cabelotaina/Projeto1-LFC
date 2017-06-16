package DeSimone;

import java.util.ArrayList;
import java.util.HashMap;

import automato.Automato;
import automato.Estado;
import expressao_regular.ControleER;

public abstract class DeSimone {
	
	public DeSimone() {
	}
	
	/**
	 * Função responsavel por criar o AFD apartir de uma 
	 * Expressão Regular dada, utilizando o algoritmo De Simone.
	 * 
	 * @param regEx		Expressão regular que se quer achar o AFD.
	 * @return			AFD equivalente a Expressão Regular dada.
	 */
	public static Automato createAutomaton(String regEx){
		Arvore tree = new Arvore(regEx);
		HashMap<ComposicaoEstados, ArrayList<No>> comp = new HashMap<>();//composicao de estados
		Automato af = new Automato();
		
		for(No n : tree.getListLeaves())
			af.adicionarSimbolo(n.getC());
		
		Estado sTmp = new Estado("Q0");
			af.estadoInicial(sTmp);
		ComposicaoEstados cState = new ComposicaoEstados(sTmp);
			cState.addNode(tree.getRoot());
		
		createAutomatonRec(cState, af, tree, comp);	
		
		return af;
	}

	/**
	 * Função recursiva interna que cria o AFD equivalente a ER dada.
	 * 
	 * @param sTmp		Estado atual da recursão.
	 * @param af		AFD que esta sendo criado.
	 * @param tree		Arvore base.
	 * @param comp		HashMap que contem o estado base como key e 
	 * 					a lista de nodos da sua composição como value.
	 */
	private static void createAutomatonRec(ComposicaoEstados sTmp, Automato af, Arvore tree,
			HashMap<ComposicaoEstados, ArrayList<No>> comp) {
		
		ArrayList<No> tmp;
		boolean equal = false;
		ArrayList<No> compTmp = getComposition(sTmp, tree);
		if(compTmp.size() == 0)
			return;
		
		for(ComposicaoEstados s : comp.keySet()){
			tmp = comp.get(s);
			equal = true;
			for(No n : compTmp){
				if(!tmp.contains(n))
					equal = false;
			}
			if(equal && compTmp.size() == tmp.size()){
				af.transicaoParaProximoEstado(sTmp.getState(), s.getState());
				break;
			}else if(equal && compTmp.size() != tmp.size()){
				equal = false;
			}
		}
		if(!equal){
			af.adicionarEstado(sTmp.getState());
			comp.put(sTmp, compTmp);
			
			HashMap<Character, ArrayList<Estado>> newStates = new HashMap<>();
			for(char c : af.alfabeto())
				newStates.put(c, new ArrayList<>());
			
			for(No n : compTmp){
				if(n.getC() != '$')
					newStates.get(n.getC()).add(new Estado("Q"+n.getNumero()));
				else
					af.estadoFinal(sTmp.getState());
			}
			
			ComposicaoEstados nState;
			ArrayList<ComposicaoEstados> tmp2 = new ArrayList<>();
			for(char c : newStates.keySet()){
				if(newStates.get(c).size() > 0){
					nState = new ComposicaoEstados(new Estado(newStates.get(c).toString()));
					for(No n : compTmp)
						if(n.getC() == c)
							nState.addNode(n);
					
					af.adicionarTransicao(sTmp.getState(), c, nState.getState());
					tmp2.add(nState);
				}
			}
			for(ComposicaoEstados s : tmp2)
				createAutomatonRec(s, af, tree, comp);
		}
	}
	
	/**
	 * Função interna que pega os nodos da composição de um estado 
	 * do AF.
	 * 
	 * @param sTmp		Estado que se quer achar a composição.
	 * @param tree		Arvore base.
	 * @return			Lista de nodos que são a composição do estado dado.
	 */
	private static ArrayList<No> getComposition(ComposicaoEstados sTmp, Arvore tree) {
		
		ArrayList<No> result = new ArrayList<>();
		ArrayList<NoAtravessado> traversed;
		for(No n : sTmp.getComposition()){
			traversed  = new ArrayList<>();
			if(n.getC() != '$')
				searchTree(n, !ControleER.isOperator(n.getC(),false), result, traversed, tree);
		}
		
		return result;
	}
	
	/**
	 * Função recursiva interna usada para 'explorar' a arvore
	 * gerada apatir da expressão regular que se quer criar o 
	 * AFD.
	 * 
	 * @param n			Nodo atual da recursão.
	 * @param dir		Direção de exploração da arvore.
	 * 					TRUE => subindo na arvore,
	 * 					FALSE => descendo na arvore.
	 * @param result	Nodos folhas achados pela exploração.
	 * @param traversed 
	 * @param tree		Arvore base.
	 */
	private static void searchTree(No n, boolean dir, ArrayList<No> result, 
			ArrayList<NoAtravessado> traversed, Arvore tree) {
		
		if(n == null || result.contains(n))
			return;
		
		// Verificação se o nodo ja foi percorrido nessa direção.
		NoAtravessado nt = new NoAtravessado(n, dir);
		if(traversed.contains(nt))
			return;
		else
			traversed.add(nt);
		
		if(!dir){//descida
			switch (n.getC()) {
				case '.':
					searchTree(n.getFilhoEsq(), false, result, traversed, tree);
					break;
				case '|':
					searchTree(n.getFilhoEsq(), false, result, traversed, tree); 
					searchTree(n.getFilhoDir(), false, result, traversed, tree);
					break;
				case '*':
					searchTree(n.getFilhoEsq(), false, result, traversed, tree);
					searchTree(n.getCostura(), true, result, traversed, tree); 
					break;
				case '?':
					searchTree(n.getFilhoEsq(), false, result, traversed, tree);
					searchTree(n.getCostura(), true, result, traversed, tree); 
					break;
				default: //folha
					if(!result.contains(n))
						result.add(n);
					break;
			}
		}else{//subida
			switch (n.getC()) {
			case '.':
				searchTree(n.getFilhoDir(), false, result, traversed, tree);
				break;
			case '|':
				while(n.getFilhoDir() != null){
					n = n.getFilhoDir();
				}
				searchTree(n.getCostura(), true, result, traversed, tree);
				break;
			case '*':
				searchTree(n.getFilhoEsq(), false, result, traversed, tree);
				searchTree(n.getCostura(), true, result, traversed, tree);
				break;
			case '?':
				searchTree(n.getCostura(), true, result, traversed, tree); 
				break;
			default://folha
				if(n.getC() == '$')
					result.add(n);
				else 
					searchTree(n.getCostura(), true, result, traversed, tree);
				break;
			}
		}
	} 
	
}
