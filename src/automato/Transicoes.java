package automato;

import java.util.ArrayList;
import java.util.HashMap;

public class Transicoes {
	
	private HashMap<Estado, HashMap<Character, ArrayList<Estado>>> 
		transicoes;
	
	public Transicoes(){
		this.transicoes = new HashMap<>();
	}
	
	/**
	 * Construtor que tenta copiar todas as transições da
	 * outra classe Transitions passada.
	 * 
	 * @param t		Transitions que se quer copiar.
	 */
	public Transicoes(Transicoes t){
		this.transicoes = new HashMap<>();
		HashMap<Estado, HashMap<Character, ArrayList<Estado>>>
			tmp = t.getTransitions();
		
		for(Estado s : tmp.keySet()){
			this.transicoes.put(new Estado(s), new HashMap<>());
			for(char c : tmp.get(s).keySet())
				this.transicoes.get(s).put(c, new ArrayList<>(tmp.get(s).get(c)));
		}
	}
	
	public Transicoes(
			HashMap<Estado, HashMap<Character, ArrayList<Estado>>> transitions) {
		this.transicoes = transitions;
	}
	
	// Transition
	public void adicionarTransicao(Estado current, char trigger, Estado next){
		if(!transicoes.containsKey(current))
			transicoes.put(current, new HashMap<>());
		if(!transicoes.get(current).containsKey(trigger))
			transicoes.get(current).put(trigger, new ArrayList<>());
		
		transicoes.get(current).get(trigger).add(next);
	}
	
	public void adicionarTransicoes(Transicoes t){
		HashMap<Estado, HashMap<Character, ArrayList<Estado>>>
			tmp = t.getTransitions();
		
		for(Estado s : tmp.keySet()){
			transicoes.put(new Estado(s), new HashMap<>());
			for(char c : tmp.get(s).keySet())
				transicoes.get(s).put(c, new ArrayList<>(tmp.get(s).get(c)));
		}
	}
	
	public ArrayList<Estado> obterProximosEstados(Estado current, char trigger){
		if(!transicoes.containsKey(current) || 
				!transicoes.get(current).containsKey(trigger))
			return new ArrayList<>();
		return transicoes.get(current).get(trigger);
	}
	
	public void removerTransicaoPorSimbolo(char simbol){
		for(Estado key : transicoes.keySet()){
			if(transicoes.get(key).containsKey(simbol))
				transicoes.get(key).remove(simbol);
		}
	}
	
	public void removerTransicaoPorEstado(Estado estado){
		transicoes.remove(estado);
		for(Estado estado2 : transicoes.keySet())
			for(char simbolo : transicoes.get(estado2).keySet())
				transicoes.get(estado2).get(simbolo).remove(estado);
	}
	
	/**
	 * Troca as transições que levam ao estado sOld 
	 * para irem para o estado sNew. Função utilizada
	 * pelo algoritmo De Simone.
	 * 
	 * @param antiga	Estado antigo que se quer eliminar as 
	 * 					transições que levam a ele.
	 * @param nova		Estado novo que se quer que as transições
	 * 					cheguem nele.
	 */
	public void transicaoParaProximoEstado(Estado antiga, Estado nova){
		for(Estado chave : transicoes.keySet()){
			for(char simbolo : transicoes.get(chave).keySet()){
				if(transicoes.get(chave).get(simbolo).remove(antiga))
					transicoes.get(chave).get(simbolo).add(nova);
			}
		}
	}
	
	public HashMap<Estado, HashMap<Character, ArrayList<Estado>>> getTransitions(){
		return this.transicoes;
	}

	@Override
	public String toString() {
		return transicoes.toString();
	}

	public ArrayList<String> obterTransicoesSeparadas() {
		ArrayList<String> separadas = new ArrayList<>();
		for(Estado chave : transicoes.keySet()){
			for(char simbolo : transicoes.get(chave).keySet()){
					separadas.add(chave.toString()+
							"={"+simbolo+","+
							transicoes.get(chave).get(simbolo).toString()+"}");
			}
		}
		return separadas;
	}
}
