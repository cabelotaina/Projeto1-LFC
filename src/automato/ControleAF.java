package automato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import expressao_regular.ExpressaoRegular;
import expressao_regular.ControleER;
import principal.Regular;

public class ControleAF {

	/**
	 * Cria um AF usando uma GR ou ER como base.
	 * 
	 * @param regular
	 *            'Conjunto' Regular [Gr ou Er] usada como base para fazer o AF.
	 * @return AF criado.
	 */
	public static Automato criarAutomato(Regular regular) {
		if (regular.ehExpressaoRegular())
			return ControleER.criarAutomato((ExpressaoRegular) regular);
		else
			return (Automato) regular;
	}

	/**
	 * Determiniza o AF dado.
	 * 
	 * @param automato
	 *            AF a determinizar.
	 * @return AF Determinizado equivalente ao AF dado.
	 */
	public static Automato determinizacao(Automato automato) {
		if (automato.extras().contains("AFD"))
			return automato;

		HashMap<Estado, Set<Estado>> fechos = automato.obterEpsilonFechos();

		Automato afd = new Automato(automato.alfabeto());
		afd.titulo(automato.titulo());
		afd.removerSimbolo('&'); // apos calcular os &-fechos ja pode tirar o &
									// do alfabeto

		determinizacaoRecursiva(automato, afd, fechos, fechos.get(automato.estadoInicial()), true);

		afd.extra("AFD");

		return afd;
	}

	/**
	 * Funcao recursiva interna usada para determinizar um AF.
	 * 
	 * @param automato
	 *            AF base para ser determinizado.
	 * @param automato_fd
	 *            AFD intermediario para recursão.
	 * @param fechos
	 *            HashMap contendo os estados como chaves e seus &-fechos como
	 *            values.
	 * @param estados
	 *            &-fecho do estado alvo atual da determinizacao.
	 * @param primeira
	 *            Eh a primeira execucao da funcao recursiva?
	 */
	private static void determinizacaoRecursiva(Automato automato, Automato automato_fd,
			HashMap<Estado, Set<Estado>> fechos, Set<Estado> estados, boolean primeira) {

		Estado atual = new Estado(estados.toString());

		if (automato_fd.estadoPertenceAoAutomato(atual))// para recursao
			return;

		Set<Estado> estado_novo = new HashSet<>();
		Set<Set<Estado>> ultimaAdicao = new HashSet<>();

		automato_fd.adicionarEstado(atual);
		if (primeira) // seta o 1* estado como estadoInicial
			automato_fd.adicionarEstadoInicial(atual);

		for (Estado estado : estados)
			if (automato.ehEstadoFinal(estado)) // verifica se contem estado
												// final
				automato_fd.adicionarEstadoFinal(atual);

		for (char simbolo : automato_fd.alfabeto()) {
			for (Estado estado : estados)
				for (Estado proximo_estado : automato.obterProximosEstados(estado, simbolo))
					estado_novo.addAll(fechos.get(proximo_estado));

			if (!estado_novo.isEmpty()) {
				Estado novo = new Estado(estado_novo.toString());
				automato_fd.adicionarTransicao(atual, simbolo, novo);
				ultimaAdicao.add(new HashSet<>(estado_novo));
			}
			estado_novo.clear();
		}

		for (Set<Estado> estados_para_adicionar : ultimaAdicao)
			determinizacaoRecursiva(automato, automato_fd, fechos, estados_para_adicionar, false);
	}

	/**
	 * Funcao que determiniza, caso necessario, o AF dado e retorna seu
	 * complemento.
	 * 
	 * @param estado
	 *            AF base para a operacao de complemento.
	 * @return AFD Complementado equivalente ao AF dado.
	 */
	public static Automato complemento(Automato estado) {

		Automato comp;
		
		if (!estado.extras().contains("AFD"))
			comp = determinizacao(estado);
		else
			comp = new Automato(estado);

		comp = completar(comp);
		comp.definirEstadosFinais(comp.obterEstadosNaoFinais());

		comp.extra("AFD_Comp");

		return comp;
	}

	/**
	 * Funcao interna que determiniza, caso necessario, e completa, tambem caso
	 * necessario, o AF dado.
	 * 
	 * @param automato_fd
	 *            AF base para ser completado.
	 * @return AFD Completo equivalente ao AF dado.
	 */
	private static Automato completar(Automato automato_fd) {
		/*
		 * if(afd.getExtras().contains("Complete")) return afd;
		 */

		automato_fd = determinizacao(automato_fd);

		Estado erro = new Estado("[ERRO]");
		boolean editado = false;

		for (Estado estado : automato_fd.estados()) {
			for (char simbolo : automato_fd.alfabeto()) {
				if (automato_fd.obterProximosEstados(estado, simbolo).isEmpty()) {
					automato_fd.adicionarTransicao(estado, simbolo, erro);
					editado = true;
				}
			}
		}

		if (editado) {
			automato_fd.adicionarEstado(erro);
			for (char simbolo : automato_fd.alfabeto())
				automato_fd.adicionarTransicao(erro, simbolo, erro);
		}
		automato_fd.extra("Complete");
		return automato_fd;
	}

	/**
	 * Funcao que determiniza, caso necessario, e retorna o AFD Minimo do AF
	 * dado.
	 * 
	 * @param afd
	 *            AF base a ser determinizado.
	 * @return AFD Minimo equivalente ao AF dado.
	 */
	public static Automato minimizacao(Automato afd) {
		if (afd.extras().contains("AFD_Min"))
			return afd;

		if (!afd.extras().contains("AFD"))
			afd = determinizacao(afd);
		else
			afd = new Automato(afd);

		// ferteis e alcancaveis
		// Estados inalcancaveis ja sao retirados pela determinizacao

		Set<Estado> ferteis = obterEstadosFerteis(afd);
		ArrayList<Estado> remover = new ArrayList<>();
		for (Estado estado : afd.estados())
			if (!ferteis.contains(estado))
				remover.add(estado);

		if (!remover.isEmpty()) {
			for (Estado s : remover)
				afd.removerEstados(s);
			afd.extras("AFD");
		}

		if (afd.estadoInicial() == null) // linguagem vazia
			return criarAutomatoVazio(afd.alfabeto());

		afd = completar(afd);

		ArrayList<ArrayList<Estado>> conjuntosDeEquivalencia = new ArrayList<>();

		conjuntosDeEquivalencia.add(afd.estadosFinais());
		ArrayList<Estado> tmp = afd.obterEstadosNaoFinais();
		if (tmp.size() > 0)
			conjuntosDeEquivalencia.add(tmp);

		conjuntosDeEquivalencia = calcularConjuntosDeEquivalencia(afd, conjuntosDeEquivalencia);

		Automato minimo = new Automato(afd.alfabeto());
		minimo.titulo(afd.titulo());
		HashMap<Estado, ArrayList<Estado>> estados = new HashMap<>();

		Estado estadosTmp;
		ArrayList<Estado> conjunto;

		for (int i = 0; i < conjuntosDeEquivalencia.size(); i++) {
			conjunto = conjuntosDeEquivalencia.get(i);
			if (!conjunto.get(0).nome().contains("[ERRO]")) {
				estadosTmp = new Estado("Q" + i);
				estados.put(estadosTmp, conjunto);
			}
		}

		int nSet;
		Estado tmpEstado2;
		for (Estado s : estados.keySet()) {
			minimo.adicionarEstado(s);
			for (Estado s2 : estados.get(s)) {
				if (afd.ehEstadoFinal(s2))
					minimo.adicionarEstadoFinal(s);
				if (afd.estadoInicial().equals(s2))
					minimo.adicionarEstadoInicial(s);
			}
		}

		for (Estado s : estados.keySet()) {
			estadosTmp = estados.get(s).get(0);
			for (char c : minimo.alfabeto()) {
				nSet = numeroConjuntoEquivalencia(conjuntosDeEquivalencia,
						afd.obterProximosEstados(estadosTmp, c).get(0));
				if (nSet != -1) {
					tmpEstado2 = minimo.obterEstado("Q" + nSet);
					if (tmpEstado2 != null)
						minimo.adicionarTransicao(s, c, tmpEstado2);
				}
			}
		}

		minimo.extra("AFD_Min");

		return minimo;
	}

	/**
	 * Funcao recursiva interna que calcula os 'Conjuntos de equivalencia'
	 * intermediarios para a construcao do AFD Minimo.
	 * 
	 * @param minimo
	 *            AFD Minimo intermediario.
	 * @param conjuntosEquivalencia
	 *            'Conjuntos de equivalencia' intermediarios.
	 * @return 'Conjuntos de equivalencia' final.
	 */
	private static ArrayList<ArrayList<Estado>> calcularConjuntosDeEquivalencia(Automato minimo,
			ArrayList<ArrayList<Estado>> conjuntosEquivalencia) {

		ArrayList<ArrayList<Estado>> novoConjunto = new ArrayList<>();
		ArrayList<Estado> tmp;
		boolean paraAdicionar;

		for (ArrayList<Estado> conjunto : conjuntosEquivalencia) {
			for (int i = 0; i < conjunto.size(); i++) {
				if (numeroConjuntoEquivalencia(novoConjunto, conjunto.get(i)) == -1) {
					tmp = new ArrayList<>();
					tmp.add(conjunto.get(i));
					for (int k = i + 1; k < conjunto.size(); k++) {
						paraAdicionar = true;
						for (char c : minimo.alfabeto()) {
							if (numeroConjuntoEquivalencia(conjuntosEquivalencia,
									minimo.obterProximosEstados(conjunto.get(i), c)
											.get(0)) != numeroConjuntoEquivalencia(conjuntosEquivalencia,
													minimo.obterProximosEstados(conjunto.get(k), c).get(0)))
								paraAdicionar = false;
						}
						if (paraAdicionar)
							tmp.add(conjunto.get(k));
					}
					novoConjunto.add(tmp);
				}
			}
		}

		if (conjuntosEquivalencia.size() == novoConjunto.size())
			return novoConjunto;
		else
			return calcularConjuntosDeEquivalencia(minimo, novoConjunto);
	}

	/**
	 * Retorna o numero do 'Conjunto de equivalencia' que contem o estado dado.
	 * 
	 * @param conjuntoEsquivalencia
	 *            'Conjunto de equivalencia' a ser checado.
	 * @param estado
	 *            Estado que se quer saber a qual conjunto pertence.
	 * @return
	 */
	private static int numeroConjuntoEquivalencia(ArrayList<ArrayList<Estado>> conjuntoEsquivalencia, Estado estado) {

		for (int i = 0; i < conjuntoEsquivalencia.size(); i++)
			if (conjuntoEsquivalencia.get(i).contains(estado))
				return i;

		return -1;
	}

	/**
	 * Determiniza, caso necessario, e retorna todos os estados alcançaveis do
	 * AFD dado apatir do estado inicial do mesmo.
	 * 
	 * @param afd
	 *            AF que se quer saber os estados alcançaveis.
	 * @return Lista com todos os estados alcançaveis do AFD.
	 */
	@SuppressWarnings("unused")
	private static Set<Estado> obterEstadosAcessiveis(Automato afd) {
		if (!afd.extras().contains("AFD"))
			afd = determinizacao(afd);

		Set<Estado> estadosAcessiveis = new HashSet<>();
		Set<Estado> paraAdicionar;

		estadosAcessiveis.add(afd.estadoInicial());

		do {
			paraAdicionar = new HashSet<>();
			for (Estado s : estadosAcessiveis)
				paraAdicionar.addAll(afd.obterTodosOsProximosEstados(s));
		} while (estadosAcessiveis.addAll(paraAdicionar));

		return estadosAcessiveis;
	}

	/**
	 * Determiniza, caso necessario, e retorna todos os estados férteis do AFD
	 * dado.
	 * 
	 * @param afd
	 *            AF que se quer saber os estados férteis.
	 * @return Lista com todos os estados fertéis do AFD.
	 */
	private static Set<Estado> obterEstadosFerteis(Automato afd) {
		if (!afd.extras().contains("AFD"))
			afd = determinizacao(afd);

		Set<Estado> estadosFerteis = new HashSet<>();
		Set<Estado> paraAdicionar;

		estadosFerteis.addAll(afd.estadosFinais());

		do {
			paraAdicionar = new HashSet<>();
			for (Estado s : estadosFerteis)
				paraAdicionar.addAll(afd.obterTodosOsEstadosAnteriores(s));
		} while (estadosFerteis.addAll(paraAdicionar));

		return estadosFerteis;
	}

	/**
	 * Cria um AFD que reconhece a linguagem vazia.
	 * 
	 * @param alfabeto
	 *            Alfabeto base para o AFD Vazio.
	 * @return AFD que aceita soh a linguagem vazia.
	 */
	private static Automato criarAutomatoVazio(ArrayList<Character> alfabeto) {
		Automato vazio = new Automato(alfabeto);
		Estado estado = new Estado("S");
		vazio.adicionarEstado(estado);
		vazio.adicionarEstadoInicial(estado);
		vazio.extras("AFD|AFD_Min|Vazio");
		return vazio;
	}

	/**
	 * Funcao que compara dois AFs dados.
	 * 
	 * @param afd1
	 *            AF usado para comparacao.
	 * @param afd2
	 *            AF usado para comparacao.
	 * @return TRUE caso os dois AFs sejam equivalentes.
	 */
	public static boolean compare(Automato afd1, Automato afd2) {
		Automato a1 = diferenca(afd1, afd2);
		Automato a2 = diferenca(afd2, afd1);
		Automato union = uniao(a1, a2);
		union = minimizacao(union);
		return union.extras().contains("Empty");
	}

	/**
	 * Função que retorna a diferença entre dois AFs.
	 * 
	 * @param afd1
	 *            AF usado para a diferença.
	 * @param afd2
	 *            AF usado para a diferença.
	 * @return AF equivalente a AFD1 - AFD2.
	 */
	private static Automato diferenca(Automato afd1, Automato afd2) {
		return intersecao(afd1, complemento(afd2));
	}

	/**
	 * Função que retorna a intersecção entre dois AFs.
	 * 
	 * @param afd1
	 *            AF usado para a intersecção.
	 * @param afd2
	 *            AF usado para a intersecção.
	 * @return AF equivalente a intersecção dos dois AFs dados.
	 */
	public static Automato intersecao(Automato afd1, Automato afd2) {
		afd1 = complemento(afd1);
		afd2 = complemento(afd2);
		Automato intersecao = uniao(afd1, afd2);
		intersecao = complemento(intersecao);
		intersecao.extras("AF|AFD");
		return intersecao;// minimize(inter);
	}

	/**
	 * Função que une dois alfabetos.
	 * 
	 * @param alfabeto1
	 *            Primeiro Alfabeto usado para a união.
	 * @param alfabeto2
	 *            Segundo Alfabeto usado para a união.
	 * @return Um novo alfabeto equivalente a união dos dois alfabetos dados.
	 */
	private static ArrayList<Character> fundirAlfabetos(ArrayList<Character> alfabeto1,
			ArrayList<Character> alfabeto2) {

		ArrayList<Character> novoAlfabeto = new ArrayList<>(alfabeto1);
		for (char c : alfabeto2) {
			if (!novoAlfabeto.contains(c))
				novoAlfabeto.add(c);
		}
		return novoAlfabeto;
	}

	/**
	 * Miniminiza, caso necessario, os dois AFs dados e retorna a união dos
	 * dois.
	 * 
	 * @param afd1
	 *            AF usado na união.
	 * @param afd2
	 *            AF usado na união.
	 * @return AF representando a união dos dois AFs dados.
	 */
	private static Automato uniao(Automato afd1, Automato afd2) {
		if (!afd1.extras().contains("AFD_Min"))
			afd1 = minimizacao(afd1);

		if (!afd2.extras().contains("AFD_Min"))
			afd2 = minimizacao(afd2).clone(true);
		else
			afd2 = afd2.clone(true);

		Automato union = new Automato(fundirAlfabetos(afd1.alfabeto(), afd2.alfabeto()));

		Estado newQ0 = new Estado("Q03");
		union.adicionarEstado(newQ0);
		union.adicionarEstadoInicial(newQ0);
		if (afd1.ehEstadoFinal(afd1.estadoInicial()) || afd2.ehEstadoFinal(afd2.estadoInicial()))
			union.adicionarEstadoFinal(newQ0);

		union.adicionarEstados(afd1.estados());
		union.adicionarEstados(afd2.estados());
		union.adicionarEstadosFinais(afd1.estadosFinais());
		union.adicionarEstadosFinais(afd2.estadosFinais());
		union.adicionarTransicoes(afd1.transicoes());
		union.adicionarTransicoes(afd2.transicoes());

		union.adicionarSimbolo('&');
		union.adicionarTransicao(newQ0, '&', afd1.estadoInicial());
		union.adicionarTransicao(newQ0, '&', afd2.estadoInicial());

		return union;
	}

	public static Regular definirAutomato(String titulo, String reg) {

		Automato automato = new Automato();
		
		System.out.println(reg);

		String[] split = reg.replace("*", "").replace("->", "").replace("{", "").replace("}", "").split(";");
		for (String s : split) {
			String[] aux = s.split("=");
			String[] preSimbolo = aux[1].split(",");
			Character simbolo = preSimbolo[0].charAt(0);
			automato.adicionarSimbolo(simbolo);
		}

		split = reg.split("->");
		String estadoInicialLabel = split[1].replace("*", "").split("=")[0];

		Estado estadoInicial = new Estado(estadoInicialLabel);
		automato.adicionarEstadoInicial(estadoInicial);

		String[] estadosLabel = reg.replace("{", "").replace("}", "").replace("->", "").replace("*", "")
				.replace("\\s", "").replace("[", "").replace("]", "").split(";");

		for (String e : estadosLabel) {
			String es = e.split("=")[0];
			automato.adicionarEstado(new Estado(es));
		}

		String[] estadosFinaisLabel = reg.replace("{", "").replace("}", "").replace("->", "").replace("\\s", "")
				.replace("[", "").replace("]", "").split(";");

		for (String ef : estadosFinaisLabel) {
			if (ef.contains("*")) {
				ef = ef.replace("*", "").split("=")[0];
				automato.adicionarEstadoFinal(new Estado(ef));
			}
		}

		String[] transicoesLabel = reg.replace("{", "").replace("}", "").replace("->", "").replace("->", "")
				.replace("\\s", "").replace("*", "").replace("[", "").replace("]", "").split(";");

		for (String t : transicoesLabel) {
			String[] par = t.split("=");
			
			// estado
			Estado corrente = new Estado(par[0]);

			// simbolo e destino
			String[] simboloDestino = par[1].split(",");

			Character simbolo = simboloDestino[0].charAt(0);

			Estado destino = new Estado(simboloDestino[1]);

			automato.adicionarTransicao(corrente, simbolo, destino);

		}
		
		System.out.println(automato.estados());
		
		automato.titulo(titulo);

		return automato;
	}
}
