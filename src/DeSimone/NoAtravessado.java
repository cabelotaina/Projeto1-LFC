package DeSimone;

public class NoAtravessado {
	private No no;
	private boolean direcao;
	
	public NoAtravessado(No no, boolean direcao){
		this.no = no;
		this.direcao = direcao;
	}

	public No getNode() {
		return no;
	}

	public boolean obterDirecao() {
		return direcao;
	}

	@Override
	public boolean equals(Object obj){
		if (this == obj){
			return true;
		}
		if (obj == null || getClass() != obj.getClass()){
			return false;
		}
		NoAtravessado no_atravessado = (NoAtravessado) obj;
		return this.no.equals(no_atravessado.no) && 
				this.direcao == no_atravessado.direcao;
	}
	
	@Override
	public String toString(){
		return no+" - "+( direcao?"Verdadeiro":"Falso" );
	}
}