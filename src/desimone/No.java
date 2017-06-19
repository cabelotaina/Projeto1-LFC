package desimone;

public class No {
	private No pai, esquerda, direita, costura;
	// 'label' do nodo
	private char simbolo;
	// numero usado nas folhas
	private int numeroFolhas;
	// codigo utilizado para diferenciar os nodos 
	private int innerCode;
	// ja existe uma costura que leva a este nodo?
	private boolean ehCosturado;
	private static int code;
	
	public No(){	
		innerCode = code;
		code += 1;
	}
	
	public No(char c, No pai){
		this.simbolo = c;
		this.pai = pai;
		
		innerCode = code;
		code += 1;
	}
	
	public No(No pai, No filhoEsq, No filhoDir, char c){
		this.pai = pai;
		this.esquerda = filhoEsq;
		this.direita = filhoDir;
		this.simbolo = c;
		
		innerCode = code;
		code += 1;
	}
	
	// Pai
	public No getPai() {
		return pai;
	}

	public void setPai(No pai) {
		this.pai = pai;
	}
	
	// Filho Esquerda
	public No getFilhoEsq() {
		return esquerda;
	}

	public void setFilhoEsq(No filhoEsq) {
		this.esquerda = filhoEsq;
	}
	
	// Filho Direita
	public No getFilhoDir() {
		return direita;
	}

	public void setFilhoDir(No filhoDir) {
		this.direita = filhoDir;
	}
	
	// Costura
	public No getCostura() {
		return costura;
	}

	public void setCostura(No costura) {
		this.costura = costura;
	}
	
	// Conteudo
	public char getC() {
		return simbolo;
	}

	public void setC(char c) {
		this.simbolo = c;
	}
	
	// Numero
	public int getNumero() {
		return numeroFolhas;
	}

	public void setNumero(int numero) {
		this.numeroFolhas = numero;
	}
	
	// isCosturado
	public boolean isCosturado(){
		return this.ehCosturado;
	}
	
	public void setIsCosturado(boolean c){
		this.ehCosturado = c;
	}

	@Override
	public String toString() {
		return "["+(numeroFolhas>0?numeroFolhas:"")+simbolo+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + simbolo;
		result = prime * result + numeroFolhas;
		result = prime * result + innerCode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		No other = (No) obj;
		if (simbolo != other.simbolo)
			return false;
		if (numeroFolhas != other.numeroFolhas)
			return false;
		if(innerCode != other.innerCode)
			return false;
		return true;
	}
}
