package automato;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import principal.Regular;

/**
 * Classe modelo que representa um Automato Finito.
 * 
 * @author Maurilio Atila
 * @author Wilian Kraemmer
 *
 */
public class Automato extends Regular {

	// estados do AF
	private ArrayList<Estado> estados;
	// alfabeto do AF
	private ArrayList<Character> alfabeto;
	// transições do AF
	private Transicoes transicoes;
	// estado inicial do AF
	private Estado estadoInicial;
	// estados finais do AF
	private ArrayList<Estado> estadosFinais;

	/**
	 * Construtor da classe. Recebe um outro AF como entrada e tenta clona-lo
	 * parcialmente.
	 * 
	 * @param automato
	 *            AF que sera parcialmente clonado.
	 */
	public Automato(Automato automato) {
		this(new ArrayList<>(automato.estados()), new ArrayList<>(automato.alfabeto()),
				new Transicoes(automato.transicoes()), new Estado(automato.estadoInicial()),
				new ArrayList<>(automato.estadosFinais()), automato.titulo(), automato.extras());
	}

	/**
	 * Construtor da classe. Recebe um alfabeto como entrada.
	 * 
	 * @param alfabeto
	 */
	public Automato(ArrayList<Character> alfabeto) {
		this(new ArrayList<>(), new ArrayList<>(alfabeto), new Transicoes(), null, new ArrayList<>(), "", "");
	}

	/**
	 * Construtor base.
	 */
	public Automato() {
		this(new ArrayList<>(), new ArrayList<>(), new Transicoes(), null, new ArrayList<>(), "", "");
	}

	/**
	 * Construtor completo da classe. Recebe todos os atributos da classe como
	 * entrada.
	 * 
	 * @param estados
	 *            estados do AF.
	 * @param alfabeto
	 *            alfabeto do AF.
	 * @param transicoes
	 *            transicoes do AF.
	 * @param estadosIniciais
	 *            estado inicial do AF.
	 * @param estadosFinais
	 *            estados finais do AF.
	 * @param titulo
	 *            titulo do AF.
	 * @param extras
	 *            valor 'extras' do AF.
	 */
	public Automato(ArrayList<Estado> estados, ArrayList<Character> alfabeto, Transicoes transicoes,
			Estado estadosIniciais, ArrayList<Estado> estadosFinais, String titulo, String extras) {
		super();
		this.estados = estados;
		this.alfabeto = alfabeto;
		this.transicoes = transicoes;
		this.estadoInicial = estadosIniciais;
		this.estadosFinais = estadosFinais;
		titulo(titulo);
		extras(extras);
	}

	// States
	public ArrayList<Estado> estados() {
		return estados;
	}

	public void definirEstados(ArrayList<Estado> estados) {
		this.estados = estados;
	}

	/**
	 * Retorna um estado da lista de estados do AF, usando o nome como chave de
	 * busca.
	 * 
	 * @param nome
	 *            nome do estado a ser encontrado.
	 * @return estado com o mesmo nome dado como entrada.
	 */
	public Estado getState(String nome) {
		for (Estado estado : estados) {
			if (estado.nome().equals(nome))
				return estado;
		}
		return null;
	}

	/**
	 * Adiciona um estado ao AF verificando se o estado ja não esta na lista de
	 * estados do mesmo.
	 * 
	 * @param estado
	 *            Estado a ser adicionado ao AF.
	 */
	public void adicionarEstado(Estado estado) {
		if (!estados.contains(estado))
			estados.add(estado);
	}

	/**
	 * Adiciona uma lista de estados ao AF.
	 * 
	 * @param estados
	 *            Estados a serem adicionados.
	 */
	public void adicionarEstados(ArrayList<Estado> estados) {
		for (Estado estado : estados)
			adicionarEstado(estado);
	}

	/**
	 * Remove um estado do AF. O estado é removido de todas as transições do AF
	 * como tambem da lista de estados, da lista de estados finais e caso seja o
	 * estado inicial, este tambem é setado como nulo.
	 * 
	 * @param estado
	 *            estado a ser removido.
	 */
	public void removerEstados(Estado estado) {
		transicoes.removerTransicaoPorEstado(estado);
		estados.remove(estado);
		estadosFinais.remove(estado);
		if (estado.equals(estadoInicial))
			estadoInicial = null;
	}

	/**
	 * Verifica se um dado estado pertence ao AF.
	 * 
	 * @param estado
	 *            Estado para verificação.
	 * @return TRUE caso o estado pertença ao AF.
	 */
	public boolean estadoPertenceAoAutomato(Estado estado) {
		return estados.contains(estado);
	}

	/**
	 * Ordena os estados do AF deixando os estados com nome == "FINAL" ou "ERRO"
	 * por ultimo.
	 */
	public void ordenarEstados() {
		Collections.sort(estados, new Comparator<Estado>() {
			public boolean ehOUltimo(Estado estado) {
				return estado.nome().contains("FINAL") || estado.nome().contains("ERRO");
			}

			@Override
			public int compare(Estado s1, Estado s2) {
				if (ehOUltimo(s1) && !ehOUltimo(s2))
					return 1;
				else if (!ehOUltimo(s1) && ehOUltimo(s2))
					return -1;
				else
					return 0;
			}
		});
	}

	// Alfabeto
	public ArrayList<Character> alfabeto() {
		return alfabeto;
	}

	public void adicionarAlfabeto(ArrayList<Character> alfabeto) {
		this.alfabeto = alfabeto;
	}

	/**
	 * Adiciona um novo simbolo ao alfabeto do AF verificando se este simbolo ja
	 * não pertence ao alfabeto do AF.
	 * 
	 * @param simbol
	 */
	public void adicionarSimbolo(char simbol) {
		if (!alfabeto.contains(simbol))
			alfabeto.add(simbol);
	}

	/**
	 * Remove um simbolo do alfabeto do AF. Remove tambem todas as transições
	 * com este simbolo.
	 * 
	 * @param simbolo
	 *            Simbolo a ser removido.
	 */
	public void removerSimbolo(char simbolo) {
		if (alfabeto.contains(simbolo)) {
			transicoes.removerTransicaoPorSimbolo(simbolo);
			alfabeto.remove((Character) simbolo);
		}
	}

	/**
	 * Ordena o alfabeto do AF deixando o simbolo '&' por ultimo.
	 */
	public void ordernarAlfabeto() {
		Collections.sort(this.alfabeto, new Comparator<Character>() {
			@Override
			public int compare(Character a, Character b) {
				if (a == '&' && b != '&')
					return 1;
				else if (a != '&' && b == '&')
					return -1;
				else
					return a.compareTo(b);
			}
		});
	}

	// Transicoes
	public Transicoes transicoes() {
		return transicoes;
	}

	public String transicoesString() {

		ArrayList<String> separadas = transicoes.obterTransicoesSeparadas();
		ArrayList<String> separadasAux = new ArrayList<>();
		for (String sep : separadas) {
			String[] par = sep.split("=");
			String atual = par[0];
			
			String estadoInicialString = estadoInicial.toString().replace("[","").replace("]","");
			//System.out.println(estadoInicialString);
			if (atual.contains(estadoInicialString) && estadosFinais.contains(new Estado(atual))) {
				separadasAux.add("->*" + sep);
			} else if (atual.contains(estadoInicial.toString())) {
				separadasAux.add("->" + sep);
			} else if (estadosFinais.contains(new Estado(atual))) {
				separadasAux.add("*" + sep);
			}
		}
		String superString = "{";
		for (String sep : separadasAux) {
			if (sep == separadasAux.get(separadasAux.size() - 1)) {
				superString += sep + "}";
			} else {
				superString += sep + ";";
			}
		}

		return superString;
	}

	public void definirTransicoes(Transicoes transicoes) {
		this.transicoes = transicoes;
	}

	/**
	 * Adiciona uma nova transicoes ao AF verificando se o estado current e o
	 * simbolo trigger ja existem no AF.
	 * 
	 * @param corrente
	 *            Estado 'corrente' da transicao.
	 * @param disparador
	 *            Simbolo 'disparador' da transicao.
	 * @param alvo
	 *            Estado 'alvo' do disparo da transicao.
	 */
	public void adicionarTransicao(Estado corrente, char disparador, Estado alvo) {
		if (!estados.contains(corrente) || !alfabeto.contains(disparador))
			return;

		transicoes.adicionarTransicao(corrente, disparador, alvo);
	}

	/**
	 * Adiciona um conjuto de transições ao AF.
	 * 
	 * @param transicoes
	 *            Transicoes a serem adicionadas.
	 */
	public void adicionarTransicoes(Transicoes transicoes) {
		this.transicoes.adicionarTransicoes(transicoes);
	}

	/**
	 * Retorna os &-Fechos de todos os estados do AF.
	 * 
	 * @return Um HashMap contendo o estado como key e seu &-fecho como value.
	 */
	public HashMap<Estado, Set<Estado>> obterEpsilonFechos() {
		HashMap<Estado, Set<Estado>> resultado = new HashMap<>();
		Set<Estado> tmp;
		for (Estado estado : estados) {
			tmp = new HashSet<>();
			obterEpsilonFecho(estado, tmp);
			resultado.put(estado, tmp);
		}
		return resultado;
	}

	/**
	 * Funcao interna que calcula o &-Fecho de um dado estado.
	 * 
	 * @param estado
	 *            Estado que se quer achar o &-fecho.
	 * @param lista
	 *            Lista que ira conter o &-fecho do 1* estado chamado pela
	 *            funcao.
	 */
	private void obterEpsilonFecho(Estado estado, Set<Estado> lista) {
		lista.add(estado);
		for (Estado proximo_estado : obterProximosEstados(estado, '&')) {
			if (!proximo_estado.equals(estado))
				obterEpsilonFecho(proximo_estado, lista);
		}
	}

	/**
	 * Retorna a lista de estados alcancaveis por um estado 'corrente' com um
	 * simbolo 'disparador'.
	 * 
	 * @param atual
	 *            Estado 'corrente'.
	 * @param disparador
	 *            Simbolo 'disparador'.
	 * @return Lista com os estados alcançaveis pelo estado 'corrente' usando o
	 *         simbolo 'disparador'.
	 */
	public ArrayList<Estado> obterProximosEstados(Estado atual, char disparador) {
		if (!estados.contains(atual) || !alfabeto.contains(disparador))
			return new ArrayList<>();
		return transicoes.obterProximosEstados(atual, disparador);
	}

	/**
	 * Retorna a lista de estados alcacaveis por um estado 'corrente' com todos
	 * os simbolos do alfabeto do AF.
	 * 
	 * @param atual
	 *            Estado 'corrente'.
	 * @return Lista com os estados alcançaveis pelo estado 'corrente' com todos
	 *         os simbolos do alfabeto do AF.
	 */
	public ArrayList<Estado> obterTodosOsProximosEstados(Estado atual) {
		ArrayList<Estado> resultado = new ArrayList<>();

		for (char c : alfabeto)
			resultado.addAll(transicoes.obterProximosEstados(atual, c));

		return resultado;
	}

	/**
	 * Retorna a lista com todos os estados que alcançam o estado 'corrente' com
	 * todos os simbolos do alfabeto do AF.
	 * 
	 * @param atual
	 *            Estado 'corrente'.
	 * @return Lista com os estados que alcançam o estado 'corrente' com todos
	 *         os simbolos do alfabeto do AF
	 */
	public ArrayList<Estado> obterTodosOsEstadosAnteriores(Estado atual) {
		ArrayList<Estado> resultado = new ArrayList<>();

		for (Estado estado_anterior : estados)
			if (obterTodosOsProximosEstados(estado_anterior).contains(atual))
				resultado.add(estado_anterior);

		return resultado;
	}

	/**
	 * Troca todas as transicoes que levam ao estado 'estado_antigo' para que
	 * levem agora para o estado 'estado_novo'.
	 * 
	 * @param estado_antigo
	 *            Estado ira perder as transicoes que levem a ele.
	 * @param estado_novo
	 *            Estado que ira ganhar as transicoes para ele.
	 */
	public void transicaoParaProximoEstado(Estado estado_antigo, Estado estado_novo) {
		if (estados.contains(estado_novo))
			transicoes.transicaoParaProximoEstado(estado_antigo, estado_novo);
	}

	// Estado Inicial
	public Estado estadoInicial() {
		return estadoInicial;
	}

	public void adicionarEstadoInicial(Estado startingState) {
		this.estadoInicial = startingState;
	}

	// Estados Finais
	public ArrayList<Estado> estadosFinais() {
		return estadosFinais;
	}

	public void definirEstadosFinais(ArrayList<Estado> estadosFinais) {
		this.estadosFinais = estadosFinais;
	}

	/**
	 * Adiciona um estado a lista de estados finais do AF.
	 * 
	 * @param estado
	 *            Estado a ser adicionado a lista.
	 */
	public void adicionarEstadoFinal(Estado estado) {
		if (estados.contains(estado) && !estadosFinais.contains(estado))
			estadosFinais.add(estado);
	}

	/**
	 * Adiciona uma lista de estados a lista de estados finais do AF.
	 * 
	 * @param estados
	 *            Lista de estados a ser adicionado.
	 */
	public void adicionarEstadosFinais(ArrayList<Estado> estados) {
		for (Estado estado_final : estados)
			adicionarEstadoFinal(estado_final);
	}

	/**
	 * Remove um estado da lista de estados finais do AF.
	 * 
	 * @param estado
	 *            Estado a ser removido.
	 */
	public void removerEstadoFinal(Estado estado) {
		if (estados.contains(estado) && estadosFinais.contains(estado))
			estadosFinais.remove(estado);
	}

	/**
	 * Verifica se um estado esta na lista de estados finais do AF.
	 * 
	 * @param estado
	 *            Estado que se quer verificar.
	 * @return TRUE caso o estado esteja na lista de estados finais.
	 */
	public boolean ehEstadoFinal(Estado estado) {
		return estadosFinais.contains(estado);
	}

	/**
	 * Retorna todos os estados nao finais do AF.
	 * 
	 * @return Lista com os estados nao finais do AF.
	 */
	public ArrayList<Estado> obterEstadosNaoFinais() {
		ArrayList<Estado> resultado = new ArrayList<>();
		for (Estado estado : estados)
			if (!ehEstadoFinal(estado))
				resultado.add(estado);

		return resultado;
	}

	/* Regular */
	@Override
	public boolean ehAutomato() {
		return true;
	}

	/**
	 * Clona este automato recriando todos os estados, listas e transicoes deste
	 * automato.
	 * 
	 * @param nomeDeEstadosUnicos
	 *            Para deixar os estados do clone com nomes unicos
	 * @return Novo AF clone deste.
	 */
	public Automato clone(boolean nomeDeEstadosUnicos) {
		Automato novo_automato = new Automato(alfabeto);
		HashMap<Estado, Estado> tmpHash = new HashMap<>();

		Estado tmp;
		for (Estado estado : estados) {
			if (nomeDeEstadosUnicos)
				tmp = new Estado(estado + "'");
			else
				tmp = new Estado(estado);
			novo_automato.adicionarEstado(tmp);
			if (ehEstadoFinal(estado))
				novo_automato.adicionarEstadoFinal(tmp);
			if (estadoInicial.equals(estado))
				novo_automato.adicionarEstadoInicial(tmp);
			tmpHash.put(estado, tmp);
		}

		for (Estado estado : estados) {
			for (char simbolo : alfabeto) {
				for (Estado proximo_estado : obterProximosEstados(estado, simbolo))
					novo_automato.adicionarTransicao(tmpHash.get(estado), simbolo, tmpHash.get(proximo_estado));
			}
		}

		return novo_automato;
	}

}
