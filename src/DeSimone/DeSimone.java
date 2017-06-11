package DeSimone;

import java.util.ArrayList;
import AutomatoFinito.Automato;
import AutomatoFinito.Estado;
import java.util.HashMap;

public class DeSimone {

	public static Automato createAutomato(String expressao_regular) {
		Arvore arvore = new Arvore(expressao_regular);
		HashMap<Composicao, ArrayList<No>> mapa = new HashMap<>();
		
		Automato automato = new Automato();

		for (No no : arvore.obterFolhas())
			automato.adicionar(no.simbolo());

		Estado estado = new Estado("Q0");
		automato.estadoInicial(estado);
		Composicao composicao = new Composicao(estado);
		composicao.no(arvore.obterRaiz());

		createAutomatoRec(composicao, automato, arvore, mapa);

		return automato;
	}

	private static void createAutomatoRec(Composicao composicao, Automato automato, Arvore arvore,
			HashMap<Composicao, ArrayList<No>> mapa) {
		// TODO Auto-generated method stub
		
	}


}
