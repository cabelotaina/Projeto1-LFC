package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import automato.Automato;
import automato.Tabela;
import principal.Regular;

/**
 * Interface dos paineis do lado direito da UI.
 * O conteudo deste painel muda conforme o 
 * 'Conjunto' Regular que ele possua. 
 * 
 * @author Gilney
 *
 */
@SuppressWarnings("serial")
public class RightContent extends JPanel {
	
	private Regular regular;
	
	/* Components */
	private JTextArea gr_erTextArea;
	private JTable afTable;
	
	/**
	 * Create the application.
	 */
	public RightContent(Regular regular) {
		this.regular = regular;
		initialize();
	}
	
	public RightContent(){
		initialize();
	}
	
	/**
	 * Metodos extras
	 */
	public Regular getRegular(){
		return this.regular;
	}
	
	public void setRegular(Regular regular){
		this.regular = regular;
		refresh();
	}
	
	public void refresh(){
		removeAll();
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		if(regular != null){
			if(regular.ehGramatica() || regular.ehExpressaoRegular())
				initGR_ER();
			else if(regular.ehAutomato())
				initAF();
		}else
			initDefault();
	}
	
	private void initGR_ER(){
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		gr_erTextArea = new JTextArea();
		gr_erTextArea.setText(regular.toString());
		gr_erTextArea.setEditable(false);
		scrollPane.setViewportView(gr_erTextArea);
	}
	
	private void initAF(){
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		
		afTable = new JTable(new Tabela((Automato) regular));
		afTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//AUTO_RESIZE_OFF
		afTable.setTableHeader(null);
		afTable.setRowSelectionAllowed(false);
		//afTable.setFillsViewportHeight(true);
		centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
		afTable.setDefaultRenderer(String.class, centerRenderer);
		
		packColumn(afTable, 2);
		
		scrollPane.setViewportView(afTable);
	}
	
	private void initDefault(){
		setLayout(new BorderLayout(0, 0));
		
		JLabel label = new JLabel("Opera\u00E7\u00E3o ainda n\u00E3o executada...");
		label.setFont(new Font("Tahoma", Font.BOLD, 11));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		add(label, BorderLayout.NORTH);
	}
	
	public static void packColumn(JTable table, int margin) {
		for (int column = 0; column < table.getColumnCount(); column++){
		    TableColumn tableColumn = table.getColumnModel().getColumn(column);
		    int preferredWidth = tableColumn.getMinWidth();
		    int maxWidth = tableColumn.getMaxWidth();
		 
		    for (int row = 0; row < table.getRowCount(); row++){
		        TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
		        Component c = table.prepareRenderer(cellRenderer, row, column);
		        int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
		        preferredWidth = Math.max(preferredWidth, width);
		 
		        //  We've exceeded the maximum width, no need to check other rows
		        if (preferredWidth >= maxWidth){
		            preferredWidth = maxWidth;
		            break;
		        }
		    }
		    
		    preferredWidth += margin*2;
		    preferredWidth = Math.max(preferredWidth, tableColumn.getPreferredWidth());
		    
		    tableColumn.setPreferredWidth( preferredWidth );
		}
	}
}
