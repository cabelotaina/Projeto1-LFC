package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
public class NovoAFDouER extends JDialog implements ActionListener {
	
	private Main main;
	private int type;
	private int side;
	private boolean isEdit;
	
	// Tipos que podem ser usados no construtor
	public final static int GR = 0;
	public final static int ER = 1;
	
	// Components
	private JTextField tfTitulo;
	private JTextArea textArea;
	
	/**
	 * Create the application.
	 */
	public NovoAFDouER(JFrame parent, Main main, int type, int side, 
			String titulo, String reg) {
		super(parent, true);
		this.main = main;
		this.type = type;
		this.side = side;		
		
		if(type != GR && type != ER){
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
	 * @param titulo	titulo da G.R/E.R
	 * @param reg		G.R ou E.R para ser editada
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
		
		JLabel label2 = new JLabel("Entre com "+ (type==0?"seu AFD":"sua E.R") +":");
		label2.setFont(new Font("Arial", Font.BOLD, 12));
		panel_1.add(label2, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		if(isEdit){
			this.setTitle("Editar GR/ER");
			textArea.setText(reg);
			tfTitulo.setText(titulo);
			tfTitulo.setEnabled(false);
			btnAdicionar.setText("Salvar");
			label2.setText("Edite sua "+ (type==0?"G.R":"E.R") +":");
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		try {
			if(isEdit)
				main.editGrEr(type, side, tfTitulo.getText(), textArea.getText());
			else
				main.addGrEr(type, side, tfTitulo.getText(), textArea.getText());
			dispose();
		} catch (WarningException e) {
			JOptionPane.showMessageDialog(this, 
					e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, 
					"Algum erro inesperado aconteceu.", 
					"Erro", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

}
