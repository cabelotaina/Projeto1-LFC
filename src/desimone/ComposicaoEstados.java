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
	
	private Estado estado;
	private ArrayList<No> composicao;
	
	public ComposicaoEstados(Estado state) {
		this(state, new ArrayList<>());
	}

	public ComposicaoEstados(Estado state, ArrayList<No> composition) {
		this.estado = state;
		this.composicao = composition;
	}
	
	// State
	public Estado obterEstado() {
		return estado;
	}

	public void definirEstado(Estado state) {
		this.estado = state;
	}

	// Composition
	public ArrayList<No> obterComposicao() {
		return composicao;
	}

	public void definirComposicao(ArrayList<No> composition) {
		this.composicao = composition;
	}
	
	public void adicionarNo(No n){
		this.composicao.add(n);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((composicao == null) ? 0 : composicao.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
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
		if (composicao == null) {
			if (other.composicao != null)
				return false;
		} else if (!composicao.equals(other.composicao))
			return false;
		if (estado == null) {
			if (other.estado != null)
				return false;
		} else if (!estado.equals(other.estado))
			return false;
		return true;
	}
	
}
