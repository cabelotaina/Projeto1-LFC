package DeSimone;

import java.util.ArrayList;
import AutomatoFinito.Automato;
import AutomatoFinito.Estado;
import java.util.HashMap;

public class DeSimone {

	public static Automato criarAutomato(String expressao_regular) {
		Arvore arvore = new Arvore(expressao_regular);
		HashMap<Composicao, ArrayList<No>> mapa = new HashMap<>();
		
		Automato automato = new Automato();

		for (No no : arvore.obterFolhas())
			automato.adicionar(no.simbolo());

		Estado estado = new Estado("Q0");
		automato.estadoInicial(estado);
		Composicao composicao = new Composicao(estado);
		composicao.no(arvore.obterRaiz());

		criarAutomato(composicao, automato, arvore, mapa);

		return automato;
	}

	private static void criarAutomato(Composicao composicao, Automato automato, Arvore arvore,
			HashMap<Composicao, ArrayList<No>> mapa) {
		ArrayList<No> listaNos;
		boolean igual = false;
		ArrayList<No> nosDaComposicao = obterComposicao(composicao, arvore);
		
		if (nosDaComposicao.size() == 0){
			return;
		}
		
		for (Composicao chave_composicao: mapa.keySet()){
			listaNos = mapa.get(chave_composicao);
			igual = true;
			for (No no: nosDaComposicao){
				if (!listaNos.contains(no)){
					igual = false;
				}
			}
			
			if (igual && nosDaComposicao.size() == listaNos.size()){
				automato.transicao(composicao.estado(), chave_composicao.estado());
				break;
			}
			else if (igual && nosDaComposicao.size() != listaNos.size()){
				igual = false;
			}
		}
		
		if (!igual){
			automato.estadoFinal(composicao.estado());
			mapa.put(composicao, nosDaComposicao);
			HashMap<Character, ArrayList<Estado>> novosEstados = new HashMap<>();
			for (char simbolo : automato.alfabeto()){
				novosEstados.put(simbolo, new ArrayList<>());
			}
			
			for (No no: nosDaComposicao){
				if (no.simbolo() != '$'){
					novosEstados.get(no.simbolo()).add(new Estado("Q"+no.numeroFolha()));
				}
				else {
					automato.adicionarEstadoFinal(composicao.estado());
				}
				
				Composicao composicao_estado;
				
				ArrayList<Composicao> composicoes = new ArrayList<>();
				
				for(char simbolo: novosEstados.keySet()){
					composicao_estado = new Composicao(new Estado(novosEstados.get(simbolo).toString()));
					for (No no_local: nosDaComposicao){
						if(no_local.simbolo() == simbolo){
							composicao_estado.no(no);
						}
					}
					automato.adicionar(composicao.estado(), simbolo, composicao_estado.estado());
					composicoes.add(composicao_estado);
				}
				
				for (Composicao composicao_local: composicoes){
					criarAutomato(composicao_local, automato, arvore, mapa);
				}
			}
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
