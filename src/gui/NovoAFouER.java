package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import automato.Automato;
import automato.Estado;
import principal.Main;
import principal.WarningException;

/**
 * Interface para adicionar e editar GRs e ERs.
 * 
 * @author Maurilio Atila
 * @author Wilian Kraemmer
 * 
 */
@SuppressWarnings("serial")
public class NovoAFouER extends JDialog implements ActionListener {

	private Main main;
	private int type;
	private int side;
	private boolean isEdit;

	// Tipos que podem ser usados no construtor
	public final static int AF = 0;
	public final static int ER = 1;

	// Components
	private JTextField tfTitulo;
	private JTextArea textArea;
	private JTable tabela = new JTable(20, 10);

	/**
	 * Create the application.
	 */
	public NovoAFouER(JFrame parent, Main main, int type, int side, String titulo, String reg) {
		super(parent, true);
		this.main = main;
		this.type = type;
		this.side = side;

		if (type != AF && type != ER) {
			this.dispose();
			return;
		}

		if (parent != null) {
			Dimension parentSize = parent.getSize();
			Point p = parent.getLocation();
			setLocation(p.x + parentSize.width / 3, p.y + parentSize.height / 4);
		}

		this.isEdit = (reg != null && !reg.equals(""));
		initialize(titulo, reg);
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @param titulo
	 *            titulo da G.R/E.R
	 * @param reg
	 *            G.R ou E.R para ser editada
	 */
	private void initialize(String titulo, String reg) {
		this.setAlwaysOnTop(true);
		this.setType(Type.UTILITY);
		this.setResizable(false);
		this.setTitle("Adicionar GR/ER");
		this.setSize(333, 339);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout(0, 0));

		JButton btnAdicionar = new JButton("Adicionar");
		btnAdicionar.addActionListener(this);
		this.getContentPane().add(btnAdicionar, BorderLayout.SOUTH);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JLabel label1 = new JLabel("Entrei com um titulo:");
		label1.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(label1, BorderLayout.NORTH);

		tfTitulo = new JTextField();
		panel.add(tfTitulo, BorderLayout.SOUTH);
		tfTitulo.setColumns(10);

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		JLabel label2 = new JLabel("Entre com " + (type == 0 ? "seu AFD" : "sua E.R") + ":");
		label2.setFont(new Font("Arial", Font.BOLD, 12));
		panel_1.add(label2, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);

		// se AF tabela se ER textArea

		if (this.type == ER) {
			textArea = new JTextArea();
			scrollPane.setViewportView(textArea);
		} else {

			 String[][] dados = { { "", "a", "b" }, { "*->q0", "q1", "q2" }, {
			 "q1", "q1", "q2" },
			 { "*q2", "q1", "q2" } };
			
			 int i = 0, j = 0;
			 for (String[] linha : dados) {
			
			 for (String item : linha) {
			 tabela.setValueAt(item, i, j);
			 j++;
			 }
			 i++;
			 j = 0;
			
			 }
			
			tabela.setTableHeader(null);
			scrollPane.setViewportView(tabela);
		}

		if (isEdit) {
			this.setTitle("Editar GR/ER");
			textArea.setText(reg);
			tfTitulo.setText(titulo);
			tfTitulo.setEnabled(false);
			btnAdicionar.setText("Salvar");
			label2.setText("Edite sua " + (type == 0 ? "A.F" : "E.R") + ":");
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		try {
			if (isEdit){
				main.editGrEr(type, side, tfTitulo.getText(), textArea.getText());
			}
			else { // leitura de tabela de automato

				if (this.type == ER) {
					main.addGrEr(type, side, tfTitulo.getText(), textArea.getText());
				} else if (this.type == AF) {

					DefaultTableModel modeloDaTabela = (DefaultTableModel) tabela.getModel();

					ArrayList<Character> alfabeto = new ArrayList<>();

					for (int j = 0; j < modeloDaTabela.getColumnCount(); j++) {
						String simbolo = (String) tabela.getValueAt(0, j);
						if (simbolo != null && !simbolo.isEmpty()) {
							alfabeto.add(simbolo.charAt(0));
						}
					}

					Automato automato = new Automato();
					automato.definirAlfabeto(alfabeto);

					for (int i = 0; i < modeloDaTabela.getRowCount(); i++) {

						String nomeDoEstadoAtual = (String) tabela.getValueAt(i, 0);
						String atual_string = "";
						if (i > 0 && nomeDoEstadoAtual != null && !nomeDoEstadoAtual.isEmpty()) {

							atual_string = nomeDoEstadoAtual.replace("->", "");
							atual_string = atual_string.replace("*", "");
							atual_string = atual_string.replaceAll("\\s", "");

						}

						Estado atual = new Estado(atual_string);

						if (i > 0) {
							if (nomeDoEstadoAtual == null) {
								break;
							}
							if (nomeDoEstadoAtual != null && !nomeDoEstadoAtual.isEmpty()) {
								if (nomeDoEstadoAtual.contains("*") && nomeDoEstadoAtual.contains("->")) {
									automato.adicionarEstado(atual);
									automato.estadoInicial(atual);
									automato.estadoFinal(atual);
								} else if (nomeDoEstadoAtual.contains("->")) {
									automato.adicionarEstado(atual);
									automato.estadoInicial(atual);
								} else if (nomeDoEstadoAtual.contains("*")) {
									automato.adicionarEstado(atual);
									automato.estadoFinal(atual);
								} else {
									automato.adicionarEstado(atual);
								}
							}

						}

						for (int j = 1; j < modeloDaTabela.getColumnCount(); j++) {
							if (i > 0 && !nomeDoEstadoAtual.isEmpty() && nomeDoEstadoAtual != null) {
								String alvo = (String) tabela.getValueAt(i, j);
								String simbolo = (String) tabela.getValueAt(0, j);
								if (!(alvo == null || simbolo == null)) {
									Estado destino = new Estado(alvo);
									automato.adicionarTransicao(atual, simbolo.charAt(0), destino);
								}

							}
						}
					}
					automato.titulo(tfTitulo.getText());
					main.addGrEr(type, side, tfTitulo.getText(), automato);
				}

			}
			dispose();
		} catch (WarningException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Algum erro inesperado aconteceu.", "Erro",
					JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

}
