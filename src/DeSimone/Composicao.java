package DeSimone;

import java.util.ArrayList;
import AutomatoFinito.Estado;

public class Composicao {
	
	private Estado estado;
	private ArrayList<No> composicao;
	
	public Composicao(Estado estado) {
		this(estado, new ArrayList<>());
	}

	public Composicao(Estado estado, ArrayList<No> composicao) {
		this.estado = estado;
		this.composicao = composicao;
	}
	
	public Estado estado() {
		return this.estado;
	}

	public void estado(Estado estado) {
		this.estado = estado;
	}

	// Composition
	public ArrayList<No> composicao() {
		return this.composicao;
	}

	public void composicao(ArrayList<No> composicao) {
		this.composicao = composicao;
	}
	
	public void no(No no){
		this.composicao.add(no);
	}

	@Override
	public int hashCode() {
		final int primeiro = 31;
		int resultado = 1;
		resultado = primeiro * resultado
				+ ((composicao == null) ? 0 : composicao.hashCode());
		resultado = primeiro * resultado + ((estado == null) ? 0 : estado.hashCode());
		return resultado;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		Composicao auxiliar = (Composicao) obj;
		if (composicao == null) {
			if (auxiliar.composicao != null){
				return false;
			}
		} 
		else if (!composicao.equals(auxiliar.composicao)){
			return false;
		}
		if (estado == null) {
			if (auxiliar.estado != null){
				return false;
			}
		} 
		else if (!estado.equals(auxiliar.estado)){
			return false;
		}
		return true;
	}	
}