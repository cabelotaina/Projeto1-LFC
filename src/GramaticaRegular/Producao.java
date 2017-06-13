package GramaticaRegular;

public class Producao {
	
	private String atual;
	private char gerada;
	private String proxima;

	public Producao(String atual, char gerada, String proxima) {
		this.atual = atual;
		this.gerada = gerada;
		this.proxima = proxima;
	}
	
	public String atual(){
		return this.atual;
	}
	
	public void atual(String atual){
		this.atual = atual;
	}
	
	public char gerada(){
		return this.gerada;
	}
	
	public void gerada(char gerada){
		this.gerada = gerada;
	}
	
	public String proxima(){
		return this.proxima;
	}
	
	public void proxima(String proxima) {
		this.proxima = proxima;

	}
	
	@Override
	public String toString(){
		return "Producao [Atual=" + this.atual +", gerado=" + this.gerada + ", proxima=" + this.proxima + "]"; 
	}

	@Override
	public boolean equals(Object objeto){
		if (this == objeto){
			return true;
		}
		if (objeto == null || getClass() != objeto.getClass()){
			return false;
		}
		Producao producao = (Producao) objeto;
		if (this.atual.equals(producao.atual) && this.gerada == producao.gerada && this.proxima.equals(producao.proxima)){
			return true;
		}
		return false;
	}
}
