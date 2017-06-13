package DeSimone;

import java.util.ArrayList;
import AutomatoFinito.Automato;
import AutomatoFinito.Estado;
import java.util.HashMap;

public class DeSimone {
	
	
	public static Automato automato(String expressao_regular){
		Automato automato = criarAutomato(expressao_regular);
		automato.definirTitulo(expressao_regular);
		automato.definirDescricao("Automato Finito Deterministico");
		automato.ordenarAlfabeto();
		return automato;
	}
	
	

	public static Automato criarAutomato(String expressao_regular) {
		Arvore arvore = new Arvore(expressao_regular);
		HashMap<Composicao, ArrayList<No>> mapa = new HashMap<>();
		
		Automato automato = new Automato();

		for (No no : arvore.getListLeaves()){
			automato.adicionar(no.simbolo());
		}

		Estado estado = new Estado("Q0");
		automato.estadoInicial(estado);
		Composicao composicao = new Composicao(estado);
		composicao.no(arvore.getRoot());

		criarAutomatoRecursivamente(composicao, automato, arvore, mapa);
		
		return automato;
	}

	private static void criarAutomatoRecursivamente(Composicao sTmp, Automato af, Arvore tree,
			HashMap<Composicao, ArrayList<No>> comp) {
		
		ArrayList<No> tmp;
		boolean equal = false;
		ArrayList<No> compTmp = obterComposicao(sTmp, tree);
		if(compTmp.size() == 0)
			return;
		
		for(Composicao s : comp.keySet()){
			tmp = comp.get(s);
			equal = true;
			for(No n : compTmp){
				if(!tmp.contains(n))
					equal = false;
			}
			if(equal && compTmp.size() == tmp.size()){
				af.transicao(sTmp.estado(), s.estado());
				break;
			}else if(equal && compTmp.size() != tmp.size()){
				equal = false;
			}
		}
		if(!equal){
			af.adicionar(sTmp.estado());
			comp.put(sTmp, compTmp);
			
			HashMap<Character, ArrayList<Estado>> newStates = new HashMap<>();
			for(char c : af.alfabeto())
				newStates.put(c, new ArrayList<>());
			
			for(No n : compTmp){
				if(n.simbolo() != '$')
					newStates.get(n.simbolo()).add(new Estado("Q"+n.numeroFolha()));
				else
					af.estadoFinal(sTmp.estado());
			}
			
			Composicao nState;
			ArrayList<Composicao> tmp2 = new ArrayList<>();
			for(char c : newStates.keySet()){
				if(newStates.get(c).size() > 0){
					nState = new Composicao(new Estado(newStates.get(c).toString()));
					for(No n : compTmp)
						if(n.simbolo() == c)
							nState.no(n);
					
					af.adicionar(sTmp.estado(), c, nState.estado());
					tmp2.add(nState);
				}
			}
			for(Composicao s : tmp2)
				criarAutomatoRecursivamente(s, af, tree, comp);
		}
	}
	
	private static ArrayList<No> obterComposicao(Composicao composicao, Arvore arvore) {
		ArrayList<No> listaNos = new ArrayList<>();
		ArrayList<NoAtravessado> listaAtravesados;
		
		for (No no: composicao.composicao()){
			listaAtravesados = new ArrayList<>();
			if (no.simbolo() != '$'){
				arvoreDeBusca(no.esquerda(), false, listaNos, listaAtravesados, arvore);
			}
			
		}
		return listaNos;
	}

	private static void arvoreDeBusca(No no, boolean direcao, ArrayList<No> listaNos,
			ArrayList<NoAtravessado> listaAtravesados, Arvore arvore) {
		if (no == null || listaNos.contains(no)){
			return;
		}
		
		NoAtravessado noAtravessado = new NoAtravessado(no, direcao);
		if (listaAtravesados.contains(noAtravessado)){
			return;
		}
		else{
			listaAtravesados.add(noAtravessado);
		}
		
		if (!direcao){
			switch (no.simbolo()) {
			case '.':
				arvoreDeBusca(no.esquerda(), false, listaNos, listaAtravesados, arvore);
				break;
			case '|':
				arvoreDeBusca(no.esquerda(), false, listaNos, listaAtravesados, arvore);
				arvoreDeBusca(no.direita(), false, listaNos, listaAtravesados, arvore);
				break;
			case '*':
				arvoreDeBusca(no.esquerda(), false, listaNos, listaAtravesados, arvore);
				arvoreDeBusca(no.direita(), true, listaNos, listaAtravesados, arvore);
				break;
			case '?':
				arvoreDeBusca(no.esquerda(), false, listaNos, listaAtravesados, arvore);
				arvoreDeBusca(no.direita(), true, listaNos, listaAtravesados, arvore);
				break;
			default:
				if (!listaNos.contains(no)){
					listaNos.add(no);
				}
				break;
			}
		}
		else {
			switch (no.simbolo()) {
			case '.':
				arvoreDeBusca(no.direita(), false, listaNos, listaAtravesados, arvore);
				break;
			case '|':
				while (no.direita() != null){
					no = no.direita();
				}
				arvoreDeBusca(no.costura(), true, listaNos, listaAtravesados, arvore);
				break;
			case '*':
				arvoreDeBusca(no.esquerda(), false, listaNos, listaAtravesados, arvore);
				arvoreDeBusca(no.costura(), true, listaNos, listaAtravesados, arvore);
				break;
			case '?':
				arvoreDeBusca(no.costura(), true, listaNos, listaAtravesados, arvore);
				break;
			default:
				if (no.simbolo() == '$'){
					listaNos.add(no);
				}
				else {
					arvoreDeBusca(no.costura(), true, listaNos, listaAtravesados, arvore);
				}
				break;
			}
		}
	}


}
