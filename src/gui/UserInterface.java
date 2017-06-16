package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog.ModalExclusionType;
import java.awt.FlowLayout;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

import principal.Main;
import principal.Regular;

public class UserInterface {

	private JFrame frame;
	private Main main;

	/* Components */
	private JPanel rightPanel1;
	private JPanel rightPanel2;

	private JPanel r1SubMenu1;
	private JPanel r2SubMenu1;

	private DefaultListModel<String> listModel;
	private JLabel r1Titulo;
	private JLabel r2Titulo;

	private JButton r1BtnEdit;
	private JButton r2BtnEdit;

	private JComboBox<String> r1ComboBox;
	private JComboBox<String> r2ComboBox;

	/**
	 * Create the application.
	 */
	public UserInterface(Main main) {
		this.main = main;
		initialize();
		// frame.setVisible(true);
	}

	/**
	 * Metodos extras
	 */
	public JFrame getFrame() {
		return this.frame;
	}

	public Main getMain() {
		return this.main;
	}

	private void refresh(JPanel panel) {
		panel.setVisible(false);
		panel.setVisible(true);
	}

	private int chooseSide() {
		Object[] options = { "Lado 1", "Lado 2" };
		String msg = "Qual lado voc\u00EA gostaria de usar para esta opera\u00E7\u00E3o?";

		return JOptionPane.showOptionDialog(getFrame(), msg, "Qual lado?", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	}

	public void setRightContent(int side, RightContent panel) {
		setEnabledSubMenu1(side, true);
		if (side == 1) {
			if (rightPanel1.getComponentCount() >= 2)
				rightPanel1.remove(1);
			if (panel.getRegular() != null)
				r1Titulo.setText(panel.getRegular().titulo());
			else
				r1Titulo.setText("");
			rightPanel1.add(panel, BorderLayout.CENTER);
			refresh(rightPanel1);
		} else if (side == 2) {
			if (rightPanel2.getComponentCount() >= 2)
				rightPanel2.remove(1);
			if (panel.getRegular() != null)
				r2Titulo.setText(panel.getRegular().titulo());
			else
				r2Titulo.setText("");
			rightPanel2.add(panel, BorderLayout.CENTER);
			refresh(rightPanel2);
		}
	}

	public void addInTheList(String reg) {
		listModel.addElement(reg);
	}

	public void removeOfTheList(int side, String reg) {
		listModel.removeElement(reg);
		setEnabledSubMenu1(side, false);
	}

	public void setEnabledSubMenu1(int side, boolean enabled) {
		JPanel p = (side == 1 ? r1SubMenu1 : r2SubMenu1);
		for (Component c : p.getComponents())
			c.setEnabled(enabled);

		if (enabled && main.isDumbGrEr(side)) {
			if (side == 1)
				r1BtnEdit.setEnabled(false);
			else
				r2BtnEdit.setEnabled(false);
		}
	}

	public boolean setComboBoxSelectedItem(int side, String key) {
		JComboBox<String> cBox = side == 1 ? r1ComboBox : r2ComboBox;

		if (cBox.getSelectedItem().equals(key))
			return false;
		else
			cBox.setSelectedItem(key);

		return true;
	}

	public String getComboBoxSelectedItem(int side) {
		JComboBox<String> cBox = side == 1 ? r1ComboBox : r2ComboBox;
		return cBox.getSelectedItem().toString();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setType(Type.POPUP);
		frame.setTitle("Trabalho 1 Formais");
		frame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		frame.setBounds(100, 100, 932, 527);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Menus */
		MenuListener mListener = new MenuListener(this);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnNovo = new JMenu("Novo");
		menuBar.add(mnNovo);

		JMenuItem itemGr = new JMenuItem("Automato");
		itemGr.addActionListener(mListener);
		mnNovo.add(itemGr);

		JMenuItem itemEr = new JMenuItem("Express\u00E3o Regular");
		itemEr.addActionListener(mListener);
		mnNovo.add(itemEr);

		JMenu mnOpUnaria = new JMenu("Op. Un\u00E1ria");
		menuBar.add(mnOpUnaria);

		JMenuItem itemDeterminacao = new JMenuItem("Determiniza\u00E7\u00E3o");
		itemDeterminacao.addActionListener(mListener);
		mnOpUnaria.add(itemDeterminacao);

		JMenuItem itemMinimacao = new JMenuItem("Minimiza\u00E7\u00E3o");
		itemMinimacao.addActionListener(mListener);
		mnOpUnaria.add(itemMinimacao);

		JMenuItem itemComplemento = new JMenuItem("Complemento");
		itemComplemento.addActionListener(mListener);
		mnOpUnaria.add(itemComplemento);

		JMenu mnOpBinaria = new JMenu("Op. Bin\u00E1ria");
		menuBar.add(mnOpBinaria);

		JMenuItem itemInterseccao = new JMenuItem("Intersec\u00E7\u00E3o");
		itemInterseccao.addActionListener(mListener);
		mnOpBinaria.add(itemInterseccao);

		JMenuItem itemComparacao = new JMenuItem("Compara\u00E7\u00E3o");
		itemComparacao.addActionListener(mListener);
		mnOpBinaria.add(itemComparacao);

		/* Splitters */
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(204);
		splitPane.setEnabled(false);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setDividerLocation(334);
		// splitPane_1.setEnabled(false);
		splitPane.setRightComponent(splitPane_1);

		/* Right Panels */
		RightPanelListener rp1Listener = new RightPanelListener(this, 1);
		RightPanelListener rp2Listener = new RightPanelListener(this, 2);

		// Panel1
		rightPanel1 = new JPanel();
		splitPane_1.setLeftComponent(rightPanel1);
		rightPanel1.setLayout(new BorderLayout(0, 0));

		JPanel r1Menu = new JPanel();
		rightPanel1.add(r1Menu, BorderLayout.NORTH);
		r1Menu.setLayout(new BorderLayout(0, 0));

		r1SubMenu1 = new JPanel();
		r1Menu.add(r1SubMenu1, BorderLayout.NORTH);
		r1SubMenu1.setLayout(new BoxLayout(r1SubMenu1, BoxLayout.X_AXIS));

		r1ComboBox = new JComboBox<>();// values
		r1ComboBox.setModel(
				new DefaultComboBoxModel<String>(new String[] { "GR/ER", "AF", "AFD", "AFD_Min", "AFD_Comp" }));
		r1ComboBox.setEnabled(false);
		r1ComboBox.addItemListener(rp1Listener);
		r1SubMenu1.add(r1ComboBox);

		r1BtnEdit = new JButton("Edit");
		r1BtnEdit.setEnabled(false);
		r1BtnEdit.addActionListener(rp1Listener);
		r1SubMenu1.add(r1BtnEdit);

		JButton r1BtnDel = new JButton("Del");
		r1BtnDel.setEnabled(false);
		r1BtnDel.addActionListener(rp1Listener);
		r1SubMenu1.add(r1BtnDel);

		JPanel r1SubMenu2 = new JPanel();
		FlowLayout fl_r1SubMenu2 = (FlowLayout) r1SubMenu2.getLayout();
		fl_r1SubMenu2.setAlignment(FlowLayout.LEADING);
		r1Menu.add(r1SubMenu2, BorderLayout.CENTER);

		r1Titulo = new JLabel("Sem conteudo...");
		r1SubMenu2.add(r1Titulo);

		// Panel2
		rightPanel2 = new JPanel();
		splitPane_1.setRightComponent(rightPanel2);
		rightPanel2.setLayout(new BorderLayout(0, 0));

		JPanel r2Menu = new JPanel();
		rightPanel2.add(r2Menu, BorderLayout.NORTH);
		r2Menu.setLayout(new BorderLayout(0, 0));

		r2SubMenu1 = new JPanel();
		r2Menu.add(r2SubMenu1, BorderLayout.NORTH);
		r2SubMenu1.setLayout(new BoxLayout(r2SubMenu1, BoxLayout.X_AXIS));

		r2ComboBox = new JComboBox<>();// values
		r2ComboBox.setModel(
				new DefaultComboBoxModel<String>(new String[] { "GR/ER", "AF", "AFD", "AFD_Min", "AFD_Comp" }));
		r2ComboBox.setEnabled(false);
		r2ComboBox.addItemListener(rp2Listener);
		r2SubMenu1.add(r2ComboBox);

		r2BtnEdit = new JButton("Edit");
		r2BtnEdit.setEnabled(false);
		r2BtnEdit.addActionListener(rp2Listener);
		r2SubMenu1.add(r2BtnEdit);

		JButton r2BtnDel = new JButton("Del");
		r2BtnDel.setEnabled(false);
		r2BtnDel.addActionListener(rp2Listener);
		r2SubMenu1.add(r2BtnDel);

		JPanel r2SubMenu2 = new JPanel();
		FlowLayout fl_r2SubMenu2 = (FlowLayout) r2SubMenu2.getLayout();
		fl_r2SubMenu2.setAlignment(FlowLayout.LEADING);
		r2Menu.add(r2SubMenu2, BorderLayout.SOUTH);

		r2Titulo = new JLabel("Sem conteudo...");
		r2SubMenu2.add(r2Titulo);

		/* Left Panel */
		JPanel leftPanel = new JPanel();
		splitPane.setLeftComponent(leftPanel);
		leftPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		leftPanel.add(scrollPane, BorderLayout.CENTER);

		listModel = new DefaultListModel<>();
		JList<String> listReg = new JList<>(listModel);

		listReg.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listReg.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				if (evt.getClickCount() == 2) {
					int side = chooseSide() + 1;
					if (side != 0)
						main.showRightContent(side, (String) list.getSelectedValue());
				}
			}
		});
		scrollPane.setViewportView(listReg);
	}

	/**
	 * Classe responsavel por cuidar dos eventos gerados pelo menu do programa.
	 * 
	 * @author Maurilio Atila
	 * @author Wilian Kraemmer
	 *
	 */
	public class MenuListener implements ActionListener {

		private UserInterface ui;
		// variavel usada para o usuario nao poder
		// executar duas operaçoes ao mesmo tempo
		private boolean isDoingSomething;

		public MenuListener(UserInterface ui) {
			this.ui = ui;
			this.isDoingSomething = false;
		}

		private boolean isBinOp(String cmd) {
			return (cmd.equals("Intersec\u00E7\u00E3o") || cmd.equals("Compara\u00E7\u00E3o"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();

			if (this.isDoingSomething)
				return;

			this.isDoingSomething = true;

			int side = -1;
			if (!isBinOp(cmd)) {
				side = chooseSide() + 1;
				if (side == 0) { // usuario fechou a janela
					this.isDoingSomething = false;
					return;
				}
			} else if (!main.haveTwoAFs()) {
				JOptionPane.showMessageDialog(ui.getFrame(), "Esta opera\u00E7\u00E3o requer duas GR/ER.");
			}

			try {
				switch (cmd) {
				case "Automato":
					new NovoAFouER(frame, main, 0, side, null, null);
					break;
				case "Express\u00E3o Regular":
					new NovoAFouER(frame, main, 1, side, null, null);
					break;
				case "Determiniza\u00E7\u00E3o":
					main.determinize(side);
					break;
				case "Minimiza\u00E7\u00E3o":
					main.minimize(side);
					break;
				case "Complemento":
					main.complement(side);
					break;
				case "Intersec\u00E7\u00E3o":
					main.intersection();
					break;
				case "Compara\u00E7\u00E3o":
					boolean eq = main.compare();
					if (eq)
						JOptionPane.showMessageDialog(frame, "As duas linguagens s\u00E3o equivalentes.");
					else
						JOptionPane.showMessageDialog(frame, "As duas linguagens n\u00E3o s\u00E3o equivalentes.");
					break;
				}
			} catch (Exception exc) {
				exc.printStackTrace();
			}
			this.isDoingSomething = false;
		}
	}

	/**
	 * Classe responsavel por cuidar dos eventos gerados pelos botoes de Edit,
	 * Del e a ComboBox dos paineis da interface.
	 * 
	 * @author Maurilio Atila
	 * @author Wilian Kraemmer
	 * 
	 * 
	 */
	public class RightPanelListener implements ActionListener, ItemListener {

		private UserInterface ui;
		private int side;

		public RightPanelListener(UserInterface ui, int side) {
			this.ui = ui;
			this.side = side;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();

			try {
				switch (cmd) {
				case "Edit":
					Regular reg = main.getRegular(side, "GR/ER");
					int type = reg.ehGramatica() ? 0 : 1;
					new NovoAFouER(frame, main, type, side, reg.titulo(), reg.toString());
					break;
				case "Del":
					int op = JOptionPane.showConfirmDialog(frame, "Voce tem certeza que deseja deletar esta GR/ER?");
					if (op == 0)
						main.deleteGrEr(side);
					break;
				}
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED)
				main.showRightContent(side, e.getItem().toString());
		}
	}

}
