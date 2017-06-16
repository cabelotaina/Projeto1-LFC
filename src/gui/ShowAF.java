package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Interface usada para mostrar o AF da intersecção
 * de dois outros AFs.
 * 
 * @author Maurilio Atila
 * @author Wilian Kraemmer
 * 
 *
 */
@SuppressWarnings("serial")
public class ShowAF extends JDialog {
	
	private RightContent content;
	
	/**
	 * Create the application.
	 */
	public ShowAF(JFrame parent, RightContent content) {
		this.content = content;
		
		if (parent != null) {
			Dimension parentSize = parent.getSize(); 
		    Point p = parent.getLocation(); 
		    setLocation(p.x + parentSize.width / 3, p.y + parentSize.height / 4);
		}
		
		initialize();
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setAlwaysOnTop(true);
		this.setType(Type.UTILITY);
		this.setResizable(false);
		this.setTitle("Automato");
		
		this.setSize(386, 376);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.getContentPane().setLayout(new BorderLayout(0, 0));
		this.getContentPane().add(content, BorderLayout.CENTER);
	}

}
