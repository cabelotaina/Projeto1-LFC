package desimone;

public class NoAtravesado {
	private No n;
	private boolean dir;
	
	public NoAtravesado(No n, boolean dir){
		this.n = n;
		this.dir = dir;
	}

	public No getNode() {
		return n;
	}

	public boolean getDir() {
		return dir;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		NoAtravesado other = (NoAtravesado)obj;
		return this.n.equals(other.n) && this.dir == other.dir;
	}
	
	@Override
	public String toString() {
		return n+"-"+(dir?"TRUE":"FALSE");
	}
}
