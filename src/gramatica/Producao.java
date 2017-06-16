package gramatica;

public class Producao {
	
	private String atual;
	private char simbolo;
	private String proximo;
	
	public Producao(String atual, char simbolo, String proximo) {
		this.atual = atual;
		this.simbolo = simbolo;
		this.proximo = proximo;
	}

	/* Simbolo Atual */
	public String obterAtual() {
		return atual;
	}
	
	public void definirAtual(String atual) {
		this.atual = atual;
	}
	
	/* Simbolo */
	public char obterSimbolo() {
		return simbolo;
	}
	
	public void definirSimbolo(char simbolo) {
		this.simbolo = simbolo;
	}
	
	/* Proximo Simbolo */
	public String obterProximo() {
		return proximo;
	}
	
	public void definirProximo(String proximo) {
		this.proximo = proximo;
	}

	@Override
	public String toString() {
		return "Production [current=" + atual + ", generated=" + simbolo
				+ ", next=" + proximo + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Producao outro = (Producao) obj;
		if( this.atual.equals(outro.obterAtual()) && 
				this.simbolo == outro.obterSimbolo() &&
				this.proximo.equals(outro.obterProximo()) )
			return true;
		
		return false;
	}
}
