package ExpressaoRegular;

import Principal.Regular;

public class ExpressaoRegular extends Regular{
  private String expressao_regular;
  
  public ExpressaoRegular(String expressao_regular, String titulo){
	  super();
	  this.expressao_regular = expressao_regular;
	  this.titulo = titulo;
  }
  
  public void definir(String expressao_regular){
	  this.expressao_regular=expressao_regular;
  }
  
  public String obter(){
	  return this.expressao_regular;
  }
  
  @Override
  public boolean expressaoRegular(){
	  return true;
  }

  @Override
  public String toString(){
	  return this.expressao_regular;
  }

}
