package automato;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class Tabela extends AbstractTableModel {
	
private Automato automaton;
	
	public Tabela(Automato automaton) {
		super();
		this.automaton = automaton;
	}
	
	@Override
	public int getColumnCount() {
		if(automaton == null)
			return 0;
		return automaton.alfabeto().size() + 2;
	}

	@Override
	public int getRowCount() {
		if(automaton == null)
			return 0;
		return automaton.estados().size() + 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(automaton == null)
			return "";
		if(rowIndex == 0 && (columnIndex == 0 || columnIndex == 1)){
			if(columnIndex == 0)
				return "Final/Inicial";
			else if(columnIndex == 1)
				return "Estados";
			else
				return "";
		}else if(rowIndex == 0)
			return automaton.alfabeto().get(columnIndex-2);
		else if(columnIndex == 0){
			Estado tmp = (Estado) this.getValueAt(rowIndex, 1);
			String s = "";
			if(automaton.estadoInicial().equals(tmp))
				s += "->";
			if(automaton.ehEstadoFinal(tmp))
				s += "*";
			return s;
		}else if(columnIndex == 1){
			return automaton.estados().get(rowIndex-1);
		}else{
			Estado tmp = (Estado) this.getValueAt(rowIndex, 1);
			char trigger = (char) this.getValueAt(0, columnIndex);
			ArrayList<Estado> nexts = automaton.obterProximosEstados(tmp, trigger);
			return (nexts.isEmpty()?"--":nexts.toString());
		}
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	@Override
    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 0 || columnIndex == 1) {
            return Integer.class;
        }else {
            return String.class;
        }
    }
}
