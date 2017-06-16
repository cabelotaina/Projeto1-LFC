package DeSimone;

public class NoAtravessado {
	private No n;
	private boolean dir;
	
	public NoAtravessado(No n, boolean dir){
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
		NoAtravessado other = (NoAtravessado)obj;
		return this.n.equals(other.n) && this.dir == other.dir;
	}
	
	@Override
	public String toString() {
		return n+"-"+(dir?"TRUE":"FALSE");
	}
}
