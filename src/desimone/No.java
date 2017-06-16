package desimone;

public class No {
	private No pai, filhoEsq, filhoDir, costura;
	// 'label' do nodo
	private char c;
	// numero usado nas folhas
	private int numero;
	// codigo utilizado para diferenciar os nodos 
	private int innerCode;
	// ja existe uma costura que leva a este nodo?
	private boolean isCosturado;
	private static int code;
	
	public No(){	
		innerCode = code;
		code += 1;
	}
	
	public No(char c, No pai){
		this.c = c;
		this.pai = pai;
		
		innerCode = code;
		code += 1;
	}
	
	public No(No pai, No filhoEsq, No filhoDir, char c){
		this.pai = pai;
		this.filhoEsq = filhoEsq;
		this.filhoDir = filhoDir;
		this.c = c;
		
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
		return filhoEsq;
	}

	public void setFilhoEsq(No filhoEsq) {
		this.filhoEsq = filhoEsq;
	}
	
	// Filho Direita
	public No getFilhoDir() {
		return filhoDir;
	}

	public void setFilhoDir(No filhoDir) {
		this.filhoDir = filhoDir;
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
		return c;
	}

	public void setC(char c) {
		this.c = c;
	}
	
	// Numero
	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	// isCosturado
	public boolean isCosturado(){
		return this.isCosturado;
	}
	
	public void setIsCosturado(boolean c){
		this.isCosturado = c;
	}

	@Override
	public String toString() {
		return "["+(numero>0?numero:"")+c+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + c;
		result = prime * result + numero;
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
		if (c != other.c)
			return false;
		if (numero != other.numero)
			return false;
		if(innerCode != other.innerCode)
			return false;
		return true;
	}
}
