package desimone;

public class NoAtravessado {
	private No no;
	private boolean direcao;
	
	public NoAtravessado(No n, boolean dir){
		this.no = n;
		this.direcao = dir;
	}

	public No obterNo() {
		return no;
	}

	public boolean obterDirecao() {
		return direcao;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		NoAtravessado other = (NoAtravessado)obj;
		return this.no.equals(other.no) && this.direcao == other.direcao;
	}
	
	@Override
	public String toString() {
		return no+"-"+(direcao?"TRUE":"FALSE");
	}
}
