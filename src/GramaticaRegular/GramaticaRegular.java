package GramaticaRegular;

import java.util.ArrayList;

import Principal.Regular;

public class GramaticaRegular extends Regular {
  private ArrayList<String> Vn;
  private ArrayList<Character> Vt;
  private ArrayList<Producao> producoes;
  private String simboloInicial;
  private String gramatica;
  
  public GramaticaRegular(String titulo){
	  this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, titulo, "");
  }

  public GramaticaRegular(String titulo, String gramatica){
	  this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, titulo, gramatica);
  }
  
  public GramaticaRegular(ArrayList<String> Vn, ArrayList<String> Vt, ArrayList<Producao> producoes, String simboloInicial, String titulo, String gramatica){
	  this.Vn = Vn;
	  this.Vn = Vt;
	  this.producoes = producoes;
	  this.simboloInicial = simboloInicial;
	  this.gramatica = gramatica;
	  this.titulo = titulo;
  }
  
  public ArrayList<String> vn() {
	return this.Vn;
  }
  
  public void vn(ArrayList<String> Vn){
	  this.Vn = Vn;
  }
  
  public ArrayList<Character> vt(){
	  return this.Vt;
  }
  
  public void vt(ArrayList<Character> Vt){
	  this.Vt = Vt;
  }
  
  public ArrayList<Producao> producoes(){
	  return this.producoes;
  }
  
  public void producoes(ArrayList<Producao> producoes){
	  this.producoes = producoes;
  }
  
  public void adicionarProducao(String atual, char gerada, String proxima){
	  Producao producao = new Producao(atual, gerada, proxima);
	  if (!this.producoes.contains(producao)){
		  this.producoes.add(producao);
	  }
  }
  
  public void adicionarProducao(Producao producao){
	  if(!this.producoes.contains(producao)){
		  this.producoes.add(producao);
	  }
  }
  
  public String simboloInicial(){
	  return this.simboloInicial;
  }
  
  public void simboloInicial(String simboloInicial){
	  this.simboloInicial = simboloInicial;
  }
  
  public String gramatica(){
	  return this.gramatica;
  }
  
  public void gramatica(String gramatica){
	  this.gramatica = gramatica;
  }
  
  @Override
  public boolean ehgramatica(){
	  return true;
  }
  
}
