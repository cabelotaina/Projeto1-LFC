package automato;

public class Estado {
	private String nome;
	
	public Estado(String nome){
		this.nome = nome;
	}
	
	public Estado(Estado outro){
		this.nome = outro.nome;
	}
	
	public String nome() {
		return nome;
	}
	
	public void nome(String nome){
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Estado outro = (Estado) obj;
		return this.nome.equals(outro.nome());
	}
	
	@Override
	public String toString() {
		return this.nome; //"["+this.nome+"]";
	}

	@Override
	public int hashCode() {
		final int primeiro = 3;
		int resultado = 2;
		resultado = primeiro * resultado + (nome==null?0:nome.hashCode());
		return resultado;
	}
}
