package AutomatoFinito;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class Tabela extends AbstractTableModel {
	
private Automato automato;
	
	public Tabela(Automato automato) {
		super();
		this.automato = automato;
	}
	
	@Override
	public int getColumnCount() {
		if(automato == null)
			return 0;
		return automato.alfabeto().size() + 2;
	}

	@Override
	public int getRowCount() {
		if(automato == null)
			return 0;
		return automato.estados().size() + 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(automato == null)
			return "";
		if(rowIndex == 0 && (columnIndex == 0 || columnIndex == 1)){
			if(columnIndex == 0)
				return "Final/Inicial";
			else if(columnIndex == 1)
				return "Estados";
			else
				return "";
		}else if(rowIndex == 0)
			return automato.alfabeto().get(columnIndex-2);
		else if(columnIndex == 0){
			Estado tmp = (Estado) this.getValueAt(rowIndex, 1);
			String s = "";
			if(automato.estadoInicial().equals(tmp))
				s += "->";
			if(automato.estadoFinal(tmp))
				s += "*";
			return s;
		}else if(columnIndex == 1){
			return automato.estados().get(rowIndex-1);
		}else{
			Estado tmp = (Estado) this.getValueAt(rowIndex, 1);
			char trigger = (char) this.getValueAt(0, columnIndex);
			ArrayList<Estado> nexts = automato.proximosEstados(tmp, trigger);
			return (nexts.isEmpty()?"--":nexts.toString());
		}
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 0 || columnIndex == 1) {
            return Integer.class;
        }else {
            return String.class;
        }
    }
}
