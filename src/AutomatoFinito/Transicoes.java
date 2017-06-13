package AutomatoFinito;

import java.util.ArrayList;
import java.util.HashMap;

public class Transicoes {
  private HashMap<Estado, HashMap<Character, ArrayList<Estado>>> transicoes;
  
  public Transicoes(){
	  this.transicoes = new HashMap<>();
  }
  
  public Transicoes(Transicoes transicoes){
	 this.transicoes = new HashMap<>();
	  HashMap<Estado, HashMap<Character, ArrayList<Estado>>> conexao = transicoes.obter();
	  
	  for(Estado estado : conexao.keySet()){
		  this.transicoes.put(new Estado(estado), new HashMap<>());
		  for(char c : conexao.get(estado).keySet()){
			  this.transicoes.get(estado).put(c, new ArrayList<>(conexao.get(estado).get(c)));
		  }
	  }
  }
  
  public Transicoes(HashMap<Estado, HashMap<Character, ArrayList<Estado>>> transicoes){
	  this.transicoes = transicoes;
  }
  
  public void adicionar(Estado atual, char simbolo, Estado proximo){
	  if(!this.transicoes.containsKey(atual)){
		  this.transicoes.put(atual, new HashMap<>());
	  }
	  if(!this.transicoes.get(atual).containsKey(simbolo)){
		  this.transicoes.get(atual).put(simbolo, new ArrayList<>());
	  }
	  this.transicoes.get(atual).get(simbolo).add(proximo);
  }
  
  public void adicionar(Transicoes transicoes){
	  HashMap<Estado, HashMap<Character, ArrayList<Estado>>> aux = transicoes.obter();
	  for(Estado estado: aux.keySet()){
		  this.transicoes.put(new Estado(estado), new HashMap<>());
		  for(char c: aux.get(estado).keySet()){
			  this.transicoes.get(estado).put(c, new ArrayList<>(aux.get(estado).get(c)));
		  }
	  }
  }
  
  public ArrayList<Estado> obterProximo(Estado atual, char simbolo){
	  if(!this.transicoes.containsKey(atual) || !this.transicoes.get(atual).containsKey(simbolo)){
		  return new ArrayList<>();
	  }
	  return this.transicoes.get(atual).get(simbolo);
  }
  
  public void removerSimbolo(char simbolo){
	  for(Estado estado: this.transicoes.keySet()){
		  if(this.transicoes.get(estado).containsKey(simbolo)){
			 this.transicoes.get(estado).remove(simbolo);
		  }
	  }
  }
  
  public void removerEstado(Estado estado){
	  this.transicoes.remove(estado);
	  for(Estado estadoAux:this.transicoes.keySet()){
		  for(char c : this.transicoes.get(estadoAux).keySet()){
			  this.transicoes.get(estadoAux).get(c).remove(estado);
		  }
	  }
  }
  
  public void trocarProximos(Estado antigo, Estado novo){
	  for(Estado estado: this.transicoes.keySet()){
		  for(char c : this.transicoes.get(estado).keySet()){
			  if(this.transicoes.get(estado).get(c).remove(antigo)){
				  this.transicoes.get(estado).get(c).add(novo);
			  }
		  }
	  }
  }
  
  public HashMap<Estado, HashMap<Character, ArrayList<Estado>>> obter(){
	  return this.transicoes;
  }
  
  @Override
  public String toString(){
	  return "{ transicoes: "+this.transicoes.size()+" }";
  }
}
