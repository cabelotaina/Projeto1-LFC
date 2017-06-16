package desimone;

import java.util.ArrayList;

import automato.Estado;

/**
 * Classe usada pelo codigo De Simone. Contem
 * o estado e os nodos que o constituem.
 * 
 * @author Maurilio Atila
 * @author Wilian Kraemmer
 *
 */
public class ComposicaoEstados {
	
	private Estado state;
	private ArrayList<No> composition;
	
	public ComposicaoEstados(Estado state) {
		this(state, new ArrayList<>());
	}

	public ComposicaoEstados(Estado state, ArrayList<No> composition) {
		this.state = state;
		this.composition = composition;
	}
	
	// State
	public Estado getState() {
		return state;
	}

	public void setState(Estado state) {
		this.state = state;
	}

	// Composition
	public ArrayList<No> getComposition() {
		return composition;
	}

	public void setComposition(ArrayList<No> composition) {
		this.composition = composition;
	}
	
	public void addNode(No n){
		this.composition.add(n);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((composition == null) ? 0 : composition.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComposicaoEstados other = (ComposicaoEstados) obj;
		if (composition == null) {
			if (other.composition != null)
				return false;
		} else if (!composition.equals(other.composition))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
	
}
