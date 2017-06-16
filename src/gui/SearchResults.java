package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Interface para mostrar os resultados da
 * busca em texto feita pelo programa.
 * 
 * @author Maurilio Atila
 * @author Wilian Kraemmer
 *
 */
@SuppressWarnings("serial")
public class SearchResults extends JDialog {

	private String txt;
	private ArrayList<String> results;

	/**
	 * Create the application.
	 */
	public SearchResults(JFrame parent, String txt, ArrayList<String> results) {
		super(parent, true);
		this.txt = txt;
		this.results = results;
		
		if(results == null){
			this.dispose();
			return;
		}
		
		if (parent != null) {
			Dimension parentSize = parent.getSize(); 
		    Point p = parent.getLocation(); 
		    setLocation(p.x + parentSize.width / 3, p.y + parentSize.height / 4);
		}
		
		initialize();
		setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setAlwaysOnTop(true);
		this.setType(Type.UTILITY);
		this.setResizable(false);
		this.setSize(333, 339);
		this.setTitle("Resultados da busca");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		this.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JLabel lblStringOriginal = new JLabel("String original:  ");
		lblStringOriginal.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_1.add(lblStringOriginal);
		
		JLabel lblOriginal = new JLabel(txt);
		lblOriginal.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_1.add(lblOriginal);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JLabel lblSubstringsEncontradas = new JLabel("Sub-strings encontradas:");
		lblSubstringsEncontradas.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_2.add(lblSubstringsEncontradas);
		
		JScrollPane scrollPane = new JScrollPane();
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JTextArea textArea = new JTextArea();
		//textArea.setEnabled(false);
		textArea.setEditable(false);
		
		String txt = "";
		for(String s : results)
			txt += (s+"\n");
		textArea.setText(txt);
		scrollPane.setViewportView(textArea);
	}

}
