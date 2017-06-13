package automato;

public class Estado {
	private String name;
	
	public Estado(String name){
		this.name = name;
	}
	
	public Estado(Estado other){
		this.name = other.name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Estado other = (Estado) obj;
		return this.name.equals(other.getName());
	}
	
	@Override
	public String toString() {
		return this.name; //"["+this.name+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 3;
		int result = 2;
		result = prime * result + (name==null?0:name.hashCode());
		return result;
	}
}
