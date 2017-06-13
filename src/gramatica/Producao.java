package gramatica;

public class Producao {
	
	private String current;
	private char generated;
	private String next;
	
	public Producao(String current, char generated, String next) {
		this.current = current;
		this.generated = generated;
		this.next = next;
	}

	/* Current NT Simbol */
	public String getCurrent() {
		return current;
	}
	
	public void setCurrent(String current) {
		this.current = current;
	}
	
	/* Generated T Simbol */
	public char getGenerated() {
		return generated;
	}
	
	public void setGenerated(char generated) {
		this.generated = generated;
	}
	
	/* Next NT Simbol */
	public String getNext() {
		return next;
	}
	
	public void setNext(String next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "Production [current=" + current + ", generated=" + generated
				+ ", next=" + next + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Producao other = (Producao) obj;
		if( this.current.equals(other.getCurrent()) && 
				this.generated == other.getGenerated() &&
				this.next.equals(other.getNext()) )
			return true;
		
		return false;
	}
}
