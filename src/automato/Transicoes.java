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
	
	public void removerTransicaoPorEstado(Estado s){
		transicoes.remove(s);
		for(Estado s2 : transicoes.keySet())
			for(char c : transicoes.get(s2).keySet())
				transicoes.get(s2).get(c).remove(s);
	}
	
	/**
	 * Troca as transições que levam ao estado sOld 
	 * para irem para o estado sNew. Função utilizada
	 * pelo algoritmo De Simone.
	 * 
	 * @param sOld		Estado antigo que se quer eliminar as 
	 * 					transições que levam a ele.
	 * @param sNew		Estado novo que se quer que as transições
	 * 					cheguem nele.
	 */
	public void transicaoParaProximoEstado(Estado sOld, Estado sNew){
		for(Estado key : transicoes.keySet()){
			for(char c : transicoes.get(key).keySet()){
				if(transicoes.get(key).get(c).remove(sOld))
					transicoes.get(key).get(c).add(sNew);
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
}
