package DeSimone;

public class No {
  private No raiz, esquerda, direita, costura;
  
  private char nome;
  
  private int numeroFolha;
  
  private int id;
  
  private boolean costurado;
  
  private static int codigo;
  
  public No(){
	  id = codigo;
	  codigo++; 
  }
  
  public No(char nome, No pai){
	  this.nome = nome;
	  this.raiz = pai;
	  id = codigo;
	  codigo++;
  }
  
  public No(No pai, No esquerda, No direita, char nome){
	  this.raiz = pai;
	  this.esquerda = esquerda;
	  this.direita = direita;
	  this.nome = nome;
	  
	  id = codigo;
	  codigo++;
  }
  
  public No raiz(){
	  return raiz;
  }
  
  public void raiz(No raiz){
	  this.raiz = raiz;
  }
  
  public No esquerda(){
	  return this.esquerda;
  }
  
  public void esquerda(No esquerda){
	  this.esquerda = esquerda;
  }
  
  public No direita(){
	  return this.direita;
  }
  
  public void direita(No direita){
	  this.direita = direita;
  }
  
  public No costura(){
	  return this.costura;
  }
  
  public void costura(No costura){
	  this.costurado = true;
	  this.costura = costura;
  }
  
  public int numeroFolha(){
	  return this.numeroFolha;
  }
  
  public void numeroFolha(int numero){
	  this.numeroFolha = numero;
  }
  
  public char nome(){
	  return this.nome;
  }
  
  public void nome(char nome){
	  this.nome = nome;
  }
  
  public boolean costurado(){
	  return this.costurado;
  }
  
  @Override
  public String toString(){
	  String label;
	  if (this.numeroFolha>0){
		  label = "["+this.numeroFolha+"]";
	  }
	  else{
		  label = "["+this.nome+"]";
	  }
	  return label;
  }
  
  @Override
  public int hashCode(){
	  final int primeiro = 31;
	  int resultado = 1;
	  resultado = primeiro * resultado + this.nome;
	  resultado = primeiro * resultado + this.numeroFolha;
	  resultado = primeiro * resultado + this.id;
	  return resultado;
  }
  
  @Override
  public boolean equals(Object objeto){
	  if (this == objeto){
		  return true;
	  }
	  if (objeto == null || getClass() != objeto.getClass()){
		  return false;
	  }
	  No no = (No) objeto;
	  if (this.nome == no.nome && this.numeroFolha == no.numeroFolha && this.costurado == no.costurado){
		  return true;
	  }
	  return false; 
  }
}
