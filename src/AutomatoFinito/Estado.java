package AutomatoFinito;

public class Estado {
  private String nome;
  
  public Estado(String nome){
	  this.nome = nome;
  }
  
  public Estado(Estado automato){
	  this.nome = automato.nome;
  }
  
  public String nome(){
	  return this.nome;
  }
  
  public void definirNome(String nome){
	  this.nome = nome;
  }
  
  @Override
  public boolean equals(Object objeto){
	  if(this == objeto){
		  return true;
	  }
	  if (objeto == null || getClass() != objeto.getClass()){
		  return false;
	  }
	  Estado auxiliar = (Estado) objeto;
	  
	  return this.nome == auxiliar.nome();
  }
  
  @Override
  public String toString(){
	  return this.nome;
  }
  
  @Override
  public int hashCode(){
	  final int primeiro = 3;
	  int resultado = 2;
	  resultado = primeiro * resultado + (this.nome==null?0:this.nome.hashCode());
	  return resultado;
  }
}
