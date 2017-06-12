package Principal;

public class Regular {
  protected String titulo;
  protected String descricao;
  

  public String obterTitulo(){
	  return this.titulo;
  }
  
  public void definirTitulo(String titulo){
	  this.titulo = titulo;
  }
  
  public String obterDescricao(){
	  return this.descricao;
  }
  
  public void definirDescricao(String descricao){
	  this.descricao = descricao;
  }
  
  public void incrementaDescricao(String incremento){
	  if(this.descricao == ""){
		  this.descricao = incremento;
	  } else {
		this.descricao = "//"+incremento;
	}
  }
  public boolean ehautomato() {
	  return false;
  }

  public boolean ehexpressaoRegular() {
	  return false;
  }
  
  public boolean ehgramatica(){
	  return false;
  }
}
