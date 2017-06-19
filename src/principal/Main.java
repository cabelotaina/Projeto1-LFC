package principal;

import java.util.ArrayList;
import java.util.HashMap;

import automato.Automato;
import automato.ControleAF;
import banco_de_dados.RegularDao;
import desimone.DeSimone;
import expressao_regular.ControleER;
import gui.RightContent;
import gui.ShowAF;
import gui.UserInterface;

/**
 * Classe responsavel pelo controle de dados do programa.
 * 
 * @author Maurilio Atila
 * @author Wilian Kraemmer
 * 
 */
public class Main {

	// interface grafica do programa
	private UserInterface ui;
	// os 2 paineis contendo as Grs/Ers/Afs do programa
	private ArrayList<HashMap<String, RightContent>> panels;
	// 'cache' contendo as Grs/Ers altualmente no programa
	private HashMap<String, Regular> regulares;
	// classe para interação com banco de dados
	private RegularDao dao;

	public static void main(String[] args) {
		// Automato automato = DeSimone.criarAutomato("b*|a*");
		// ControleAF.definirAutomato("Teste Maurilio",
		// automato.transicoesString());
		// System.out.println(automato.titulo());
		new Main();
		//new Main("DEBUG");
	}

	public Main(String string) {
		this.dao = new RegularDao();
		try {
			dao.removeTudo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Main() {
		this.ui = new UserInterface(this);
		this.panels = new ArrayList<>();
		this.regulares = new HashMap<>();
		this.dao = new RegularDao();
		initHashs();
		initList();
		ui.getFrame().setVisible(true);
	}

	/**
	 * Inicializa a lista de Grs/Ers pegando as informações do banco de dados.
	 */
	private void initList() {
		ArrayList<Regular> regs = null;
		try {
			regs = dao.obterTudo();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (regs != null && regs.size() > 0) {
			for (Regular r : regs) {
				System.out.println(r.titulo());
				internalAddGrEr(r);
			}
		}
	}

	/**
	 * Inicializa os HashMaps dos dois paines do programa.
	 */
	private void initHashs() {
		HashMap<String, RightContent> l1 = new HashMap<>();
		l1.put("ER", new RightContent());
		l1.put("AF", new RightContent());
		l1.put("AFD", new RightContent());
		l1.put("AFD_Min", new RightContent());
		l1.put("AFD_Comp", new RightContent());

		panels.add(l1);

		HashMap<String, RightContent> l2 = new HashMap<>();
		l2.put("ER", new RightContent());
		l2.put("AF", new RightContent());
		l2.put("AFD", new RightContent());
		l2.put("AFD_Min", new RightContent());
		l2.put("AFD_Comp", new RightContent());

		panels.add(l2);
	}

	/**
	 * Retorna o 'Conjunto' Regular dependendo do lado e da chave.
	 * 
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @param key
	 *            Qual 'key'? AF/ER, AF, AFD, AFD_Min, AFD_Comp.
	 * @return Retorna o 'conjunto' regular.
	 */
	public Regular getRegular(int side, String key) {
		return panels.get(side - 1).get(key).getRegular();
	}

	/**
	 * Limpa todos os paineis de um dos lados.
	 * 
	 * @param side
	 *            Qual lado? 1 ou 2.
	 */
	private void cleanExtraPanels(int side) {
		HashMap<String, RightContent> panel = panels.get(side - 1);
		panel.get("ER").setRegular(null);
		panel.get("AF").setRegular(null);
		panel.get("AFD").setRegular(null);
		panel.get("AFD_Min").setRegular(null);
		panel.get("AFD_Comp").setRegular(null);
	}

	/**
	 * Adiciona um novo 'Conjunto' Regular ao 'cache' do programa e a lista.
	 * 
	 * @param r
	 *            'Conjunto' Regular a ser adicionado.
	 */
	private void internalAddGrEr(Regular r) {
		if (r == null) {
			return;
		}

		regulares.put(r.titulo(), r);
		ui.addInTheList(r.titulo());
	}

	/**
	 * Adicionada uma nova AF/ER passada pelo usuario.
	 * 
	 * @param type
	 *            Qual o tipo do 'Conjunto' Regular? 0 = AF, 1 = ER.
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @param titulo
	 *            Titulo do novo 'Conjunto' Regular.
	 * @param reg
	 *            Automato ou Expressao regular a ser adicionada.
	 * 
	 * @throws WarningException
	 *             Caso ja exista uma Af/Er com mesmo titulo.
	 * @throws WarningException
	 *             Caso haja um erro com a Af/Er entrada pelo usuario.
	 * @throws Exception
	 *             Caso haja um erro vindo do banco de dados.
	 */
	public void addAfEr(int type, int side, String titulo, Object reg) throws Exception {
		Regular regular = null;

		if (titulo == null || titulo.equals(""))
			throw new WarningException("Por favor entre com um titulo.");

		titulo = (type == 0 ? "AF: " : "ER: ") + titulo;

		if (regulares.containsKey(titulo)) // deixar substituir?
			throw new WarningException("Ja existe uma " + (type == 0 ? "automato" : "express\u00E3o")
					+ " com este titulo, por favor escolha outro.");

		if (type == 0) {
			regular = (Regular) reg;
			regular.titulo(titulo);
		} else if (type == 1) {
			regular = ControleER.criarExpressaoRegular(titulo, (String) reg);
		}

		if (regular == null) {
			throw new WarningException("Parece haver algum erro com sua " + (type == 0 ? "automato" : "express\u00E3o")
					+ ". Por favor reanalize-a e tente novamente.");
		}

		regular.extra("");

		regulares.put(titulo, regular);
		ui.addInTheList(titulo);

		dao.adicionarRegular(regular);
		if (type == 0) {
			setRightContent(side, "AF", regular, true);
			createExtras(side, "AF");
		} else if (type == 1) {
			setRightContent(side, "ER", regular, true);
			createExtras(side, "ER");
		}
	}

	/**
	 * Edita uma Gr/Er ja adicionada pelo usuario.
	 * 
	 * @param type
	 *            Qual o tipo do 'Conjunto' Regular? 0 = GR, 1 = ER.
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @param titulo
	 *            Titulo da Af/Er a ser editada.
	 * @param reg
	 *            Gramatica ou Expressão regular a ser editada.
	 * 
	 * @throws WarningException
	 *             Caso haja um erro com a Gr/Er entrada pelo usuario.
	 * @throws Exception
	 *             Caso haja um erro vindo do banco de dados.
	 */
	public void editGrEr(int type, int side, String titulo, String reg) throws Exception {
		Regular regular = null;

		if (type == 0) {
			regular = ControleAF.definirAutomato(titulo, reg);
			updateRightContentPanel(side, "AF", regular, true);
		} else if (type == 1) {
			regular = ControleER.criarExpressaoRegular(titulo, reg);
			updateRightContentPanel(side, "ER", regular, true);
		}

		if (regular == null) {
			throw new WarningException("Parece haver algum erro com sua " + (type == 0 ? "gramatica" : "express\u00E3o")
					+ ". Por favor reanalize-a e tente novamente.");
		}

		regular.extras(regulares.get(titulo).extras());
		regulares.put(titulo, regular);

		dao.editarRegular(regular);

	}

	/**
	 * Deleta uma Gr/Er do 'cache', da lista e do banco de dados.
	 * 
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @throws Exception
	 *             Caso haja um erro vindo do banco de dados.
	 */
	public void deleteAfEr(int side) throws Exception {
		Regular reg = null;
		String type = null;
		if (!(getRegular(side, "ER") == null)) {
			reg = getRegular(side, "ER");
			type = "ER";
		} else {
			reg = getRegular(side, "AF");
			type = "AF";
		}

		if (reg != null) {
			regulares.remove(reg.titulo());
			setRightContent(side, type, null, true);
			ui.removeOfTheList(side, reg.titulo());
			dao.removeRegular(reg);
		}
	}

	/**
	 * Cria os AFs extras de uma Gr/Er ou de um AF Intersecção.
	 * 
	 * @param side
	 *            Qual lado? 1 ou 2.
	 */
	private void createExtras(int side, String type) {
		Regular reg = getRegular(side, type);
		String extras = reg.extras();
		String titule = reg.titulo();

		if (extras.equals("") || extras.contains("AF") || titule.contains("AF"))
			panels.get(side - 1).get("AF").setRegular(ControleAF.criarAutomato(reg));

		Automato af = (Automato) getRegular(side, "AF");

		if (extras.contains("AFD"))
			panels.get(side - 1).get("AFD").setRegular(ControleAF.determinizacao(af));

		af = (Automato) getRegular(side, "AFD");
		if (extras.contains("AFD_Comp"))
			panels.get(side - 1).get("AFD_Comp").setRegular(ControleAF.complemento(af));

		if (extras.contains("AFD_Min"))
			panels.get(side - 1).get("AFD_Min").setRegular(ControleAF.minimizacao(af));
	}

	/**
	 * Funcao utilizada para mudar o conteudo de um painel. Pode tanto ser
	 * chamada pelo usuario clicando 2x num elemento da lista, como pelo usuario
	 * mudando o valor de um dos comboBox.
	 * 
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @param key
	 *            Qual 'key'? AF/ER, AF, AFD, AFD_Min, AFD_Comp.
	 */
	public void showRightContent(int side, String key) {
		if (regulares.containsKey(key)) {// left list [key == titulo]
			Regular reg = regulares.get(key);
			if (regulares.get(key).ehAutomato()) {
				Regular automato = ControleAF.definirAutomato(reg.titulo(), ((Automato) reg).transicoesString());
				setRightContent(side, "AF", automato, true);
				// setRightContent(side, "AF/ER", automato, true);
				// setRightContent(side, "AF", automato, false);
				createExtras(side, "AF");
			} else {
				setRightContent(side, "ER", reg, true);
				createExtras(side, "ER");
			}
		} else if (panels.get(side - 1).containsKey(key)) {// change on comboBox
			RightContent panel = panels.get(side - 1).get(key);
			ui.setRightContent(side, panel);
		}
	}

	/**
	 * Funcao utilizada internamente para mudar o conteudo de um dos paineis.
	 * 
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @param key
	 *            Qual 'key'? ER, AF, AFD, AFD_Min, AFD_Comp.
	 * @param reg
	 *            'Conjunto' Regular que ira ser adicionado ao painel.
	 * @param clean
	 *            Devesse limpar os outros paineis?
	 */
	private void setRightContent(int side, String key, Regular reg, boolean clean) {
		RightContent panel = panels.get(side - 1).get(key);

		if (clean)
			cleanExtraPanels(side);
		if (reg != null)
			panel.setRegular(reg);

		if (!ui.setComboBoxSelectedItem(side, key)) // caso ja esteja no panel
													// do
													// 'key', entao da refresh
													// no panel
			ui.setRightContent(side, panel);
	}

	/**
	 * Função utilizada para mudar o conteudo de um dos paineis. Caso os 2
	 * paineis sejam iguais, atualiza o outro painel tambem.
	 * 
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @param key
	 *            Qual 'key'? AF/ER, AF, AFD, AFD_Min, AFD_Comp.
	 * @param regular
	 *            'Conjunto' Regular que ira ser adicionado ao painel.
	 * @param clean
	 *            Devesse limpar os outros paineis?
	 */
	private void updateRightContentPanel(int side, String key, Regular regular, boolean clean) {
		if (ehOMesmoAutomatoEmAmbosOsPaineis()) {// ms AF/ER nos 2 lados
			setRightContent((side % 2) + 1, key, regular, clean);
			createExtras((side % 2) + 1, key);
		}
		setRightContent(side, key, regular, clean);
		createExtras(side, key);
	}

	/**
	 * Determiniza o AF de um dos lados do programa. Caso os dois lados sejam
	 * iguais, atualiza os dois. Esta função atualiza o valor de 'Extras' da
	 * Gr/Er.
	 * 
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @throws Exception
	 *             Caso haja um erro vindo do banco de dados.
	 */
	public void determinize(int side) throws Exception {
		determinize(side, true);
	}

	/**
	 * Determiniza o AF de um dos lados do programa. Caso os dois lados sejam
	 * iguais, atualiza os dois.
	 * 
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @param updateExtras
	 *            Devesse atualizar o valor de 'Extras' da Gr/Er?
	 * @throws Exception
	 *             Caso haja um erro vindo do banco de dados.
	 */
	private void determinize(int side, boolean updateExtras) throws Exception {
		
		
		
		if (getRegular(side, "AFD") != null) { // AFD ja esta criado, entao
													// soh mude para o panel
													// dele
			setRightContent(side, "AFD", null, false);
			return;
		}

		Automato afnd = (Automato) getRegular(side, "AF");
		System.out.println(afnd.alfabeto());
		setRightContent(side, "AFD", ControleAF.determinizacao(afnd), false);

		if (updateExtras) {
			addExtra(side, "AFD");
			dao.definirExtras(getRegular(side, "AF"));
		}

		if (ehOMesmoAutomatoEmAmbosOsPaineis())
			determinize((side % 2) + 1, false);
	}

	/**
	 * Determiniza, caso necessario, e complementa o AF de um dos lados. Caso os
	 * dois lados sejam iguais, atualiza os dois. Esta função atualiza o valor
	 * de 'Extras' da Gr/Er.
	 *
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @throws Exception
	 *             Caso haja um erro vindo do banco de dados.
	 */
	public void complemento(int side) throws Exception {
		complemento(side, true);
	}

	/**
	 * Determiniza, caso necessario, e complementa o AF de um dos lados. Caso os
	 * dois lados sejam iguais, atualiza os dois.
	 *
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @param updateExtras
	 *            Devesse atualizar o valor de 'Extras' da Af/Er?
	 * @throws Exception
	 *             Caso haja um erro vindo do banco de dados.
	 */
	private void complemento(int side, boolean updateExtras) throws Exception {
		if (getRegular(side, "AF") == null)// ainda n tem AF/ER
			return;
		else if (getRegular(side, "AFD_Comp") != null) {// AFD_Comp ja esta
														// criado, entao soh
														// mude para o panel
														// dele
			setRightContent(side, "AFD_Comp", null, false);
			return;
		}

		if (getRegular(side, "AFD") == null)
			determinize(side, false);

		Automato afd = (Automato) getRegular(side, "AFD");
		setRightContent(side, "AFD_Comp", ControleAF.complemento(afd), false);

		if (updateExtras) {
			addExtra(side, "AFD|AFD_Comp");
			dao.definirExtras(getRegular(side, "AF"));
		}

		if (ehOMesmoAutomatoEmAmbosOsPaineis())
			complemento((side % 2) + 1, false);
	}

	/**
	 * Determiniza, caso necessario, e Miniminiza o AF de um dos lados do
	 * programa. Caso os dois lados sejam iguais, atualiza os dois. Esta
	 * função atualiza o valor de 'Extras' da Gr/Er.
	 *
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @throws Exception
	 *             Caso haja um erro vindo do banco de dados.
	 */
	public void minimize(int side) throws Exception {
		minimize(side, true);
	}

	/**
	 * Determiniza, caso necessario, e Miniminiza o AF de um dos lados do
	 * programa. Caso os dois lados sejam iguais, atualiza os dois.
	 *
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @param updateExtras
	 *            Devesse atualizar o valor de 'Extras' da Gr/Er?
	 * @throws Exception
	 *             Caso haja um erro vindo do banco de dados.
	 */
	private void minimize(int side, boolean updateExtras) throws Exception {
		if (getRegular(side, "ER") == null)// ainda n tem AF/ER
			return;
		else if (getRegular(side, "AFD_Min") != null) {// AFD_Min ja esta
														// criado, entao soh
														// mude para o panel
														// dele
			setRightContent(side, "AFD_Min", null, false);
			return;
		}

		if (getRegular(side, "AFD") == null)// Minimizacao precisa de um AFD
			determinize(side, false);

		Automato afd = (Automato) getRegular(side, "AFD");
		setRightContent(side, "AFD_Min", ControleAF.minimizacao(afd), false);

		if (updateExtras) {
			addExtra(side, "AFD|AFD_Min");
			dao.definirExtras(getRegular(side, "AF"));
		}

		if (ehOMesmoAutomatoEmAmbosOsPaineis())
			minimize((side % 2) + 1, false);
	}

	/**
	 * Miniminiza, caso necessario, os dois lados e Compara suas Grs/Ers.
	 * 
	 * @return TRUE caso lado1 == lado2, FALSE caso contrario.
	 * @throws Exception
	 *             Caso haja um erro vindo do banco de dados casada pela
	 *             minimização.
	 */
	public boolean compare() throws Exception {
		if (getRegular(1, "AFD_Min") == null)// melhor usar os AFDs minimos para
												// comparar...
			minimize(1, true);
		if (!ehOMesmoAutomatoEmAmbosOsPaineis() && getRegular(2, "AFD_Min") == null)
			minimize(2, true);

		Automato afd1 = (Automato) getRegular(1, "AFD_Min");
		Automato afd2 = (Automato) getRegular(2, "AFD_Min");

		return ControleAF.compare(afd1, afd2);
	}

	/**
	 * Miniminiza, caso necessario, e faz a intersecção dos dois lados.
	 * 
	 * @throws Exception
	 *             Caso haja um erro vindo do banco de dados.
	 */
	public void intersecao() throws Exception {
		if (getRegular(1, "AFD_Min") == null)// melhor usar os AFDs minimos para
												// comparar...
			minimize(1, true);
		if (!ehOMesmoAutomatoEmAmbosOsPaineis() && getRegular(2, "AFD_Min") == null)
			minimize(2, true);

		Automato afd1 = (Automato) getRegular(1, "AFD_Min");
		Automato afd2 = (Automato) getRegular(2, "AFD_Min");

		Automato inter = ControleAF.intersecao(afd1, afd2);
		RightContent content = new RightContent(inter);

		inter.titulo("AF: " + afd1.titulo() + " /\\ " + afd2.titulo());
		regulares.put(inter.titulo(), inter);
		ui.addInTheList(inter.titulo());

		new ShowAF(ui.getFrame(), content);
	}

	/**
	 * Verifica se os dois lados tem a mesma Gr/Er
	 * 
	 * @return
	 */
	private boolean ehOMesmoAutomatoEmAmbosOsPaineis() {
		Regular r1 = getRegular(1, "AF");
		Regular r2 = getRegular(2, "AF");

		if (r1 != null && r2 != null && r2.titulo().equals(r1.titulo())) {
			return true;
		}
		return false;
	}

	/**
	 * Verifica se a Gr/Er de um dos lados esta vazia, isto é utilizado para a
	 * intersecção.
	 * 
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @return TRUE caso a Gr/Er esta vazia.
	 */
	public boolean isDumbGrEr(int side) {
		Regular reg = getRegular(side, "AF/ER");
		return reg == null ? false : reg.isDumbGrEr();
	}

	/**
	 * Verifica se os dois lados tem AFs.
	 * 
	 * @return TRUE caso os dois lados contenham AFs.
	 */
	public boolean temDoisAutomatos() {
		return getRegular(1, "AF") != null && getRegular(2, "AF") != null;
	}

	/**
	 * Verifica se a Gr/Er de um dos lados é uma ER.
	 * 
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @return TRUE caso seja uma ER.
	 */
	public boolean isRegExpression(int side) {
		return getRegular(side, "ER").ehExpressaoRegular();
	}

	/**
	 * Adiciona um valor 'extra' a Gr/Er do lado expecificado.
	 * 
	 * @param side
	 *            Qual lado? 1 ou 2.
	 * @param extra
	 *            Valor 'extra' a ser adicionado.
	 */
	private void addExtra(int side, String extra) {
		Regular reg = getRegular(side, "AF");

		reg.extra(extra);
	}
}
