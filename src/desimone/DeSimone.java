package desimone;

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
	public static Automato criarAutomato(String regEx){
		Arvore tree = new Arvore(regEx);
		HashMap<ComposicaoEstados, ArrayList<No>> comp = new HashMap<>();//composicao de estados
		Automato af = new Automato();
		
		for(No n : tree.obterListaDeFolhas())
			af.adicionarSimbolo(n.getC());
		
		Estado sTmp = new Estado("Q0");
			af.adicionarEstadoInicial(sTmp);
		ComposicaoEstados cState = new ComposicaoEstados(sTmp);
			cState.adicionarNo(tree.obterRaiz());
		
		criarAutomatoRecursivamente(cState, af, tree, comp);	
		
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
	private static void criarAutomatoRecursivamente(ComposicaoEstados sTmp, Automato af, Arvore tree,
			HashMap<ComposicaoEstados, ArrayList<No>> comp) {
		
		ArrayList<No> tmp;
		boolean equal = false;
		ArrayList<No> compTmp = obterComposicao(sTmp, tree);
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
				af.transicaoParaProximoEstado(sTmp.obterEstado(), s.obterEstado());
				break;
			}else if(equal && compTmp.size() != tmp.size()){
				equal = false;
			}
		}
		if(!equal){
			af.adicionarEstado(sTmp.obterEstado());
			comp.put(sTmp, compTmp);
			
			HashMap<Character, ArrayList<Estado>> newStates = new HashMap<>();
			for(char c : af.alfabeto())
				newStates.put(c, new ArrayList<>());
			
			for(No n : compTmp){
				if(n.getC() != '$')
					newStates.get(n.getC()).add(new Estado("Q"+n.getNumero()));
				else
					af.adicionarEstadoFinal(sTmp.obterEstado());
			}
			
			ComposicaoEstados nState;
			ArrayList<ComposicaoEstados> tmp2 = new ArrayList<>();
			for(char c : newStates.keySet()){
				if(newStates.get(c).size() > 0){
					nState = new ComposicaoEstados(new Estado(newStates.get(c).toString()));
					for(No n : compTmp)
						if(n.getC() == c)
							nState.adicionarNo(n);
					
					af.adicionarTransicao(sTmp.obterEstado(), c, nState.obterEstado());
					tmp2.add(nState);
				}
			}
			for(ComposicaoEstados s : tmp2)
				criarAutomatoRecursivamente(s, af, tree, comp);
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
	private static ArrayList<No> obterComposicao(ComposicaoEstados sTmp, Arvore tree) {
		
		ArrayList<No> result = new ArrayList<>();
		ArrayList<NoAtravessado> traversed;
		for(No n : sTmp.obterComposicao()){
			traversed  = new ArrayList<>();
			if(n.getC() != '$')
				buscarNaArvore(n, !ControleER.ehOperador(n.getC(),false), result, traversed, tree);
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
	private static void buscarNaArvore(No n, boolean dir, ArrayList<No> result, 
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
					buscarNaArvore(n.getFilhoEsq(), false, result, traversed, tree);
					break;
				case '|':
					buscarNaArvore(n.getFilhoEsq(), false, result, traversed, tree); 
					buscarNaArvore(n.getFilhoDir(), false, result, traversed, tree);
					break;
				case '*':
					buscarNaArvore(n.getFilhoEsq(), false, result, traversed, tree);
					buscarNaArvore(n.getCostura(), true, result, traversed, tree); 
					break;
				case '?':
					buscarNaArvore(n.getFilhoEsq(), false, result, traversed, tree);
					buscarNaArvore(n.getCostura(), true, result, traversed, tree); 
					break;
				default: //folha
					if(!result.contains(n))
						result.add(n);
					break;
			}
		}else{//subida
			switch (n.getC()) {
			case '.':
				buscarNaArvore(n.getFilhoDir(), false, result, traversed, tree);
				break;
			case '|':
				while(n.getFilhoDir() != null){
					n = n.getFilhoDir();
				}
				buscarNaArvore(n.getCostura(), true, result, traversed, tree);
				break;
			case '*':
				buscarNaArvore(n.getFilhoEsq(), false, result, traversed, tree);
				buscarNaArvore(n.getCostura(), true, result, traversed, tree);
				break;
			case '?':
				buscarNaArvore(n.getCostura(), true, result, traversed, tree); 
				break;
			default://folha
				if(n.getC() == '$')
					result.add(n);
				else 
					buscarNaArvore(n.getCostura(), true, result, traversed, tree);
				break;
			}
		}
	} 
	
}
