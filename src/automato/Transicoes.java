package automato;

import java.util.ArrayList;
import java.util.HashMap;

public class Transicoes {
	
	private HashMap<Estado, HashMap<Character, ArrayList<Estado>>> 
		transitions;
	
	public Transicoes(){
		this.transitions = new HashMap<>();
	}
	
	/**
	 * Construtor que tenta copiar todas as transições da
	 * outra classe Transitions passada.
	 * 
	 * @param t		Transitions que se quer copiar.
	 */
	public Transicoes(Transicoes t){
		this.transitions = new HashMap<>();
		HashMap<Estado, HashMap<Character, ArrayList<Estado>>>
			tmp = t.getTransitions();
		
		for(Estado s : tmp.keySet()){
			transitions.put(new Estado(s), new HashMap<>());
			for(char c : tmp.get(s).keySet())
				transitions.get(s).put(c, new ArrayList<>(tmp.get(s).get(c)));
		}
	}
	
	public Transicoes(
			HashMap<Estado, HashMap<Character, ArrayList<Estado>>> transitions) {
		this.transitions = transitions;
	}
	
	// Transition
	public void addTransition(Estado current, char trigger, Estado next){
		if(!transitions.containsKey(current))
			transitions.put(current, new HashMap<>());
		if(!transitions.get(current).containsKey(trigger))
			transitions.get(current).put(trigger, new ArrayList<>());
		
		transitions.get(current).get(trigger).add(next);
	}
	
	public void adicionarTransicoes(Transicoes t){
		HashMap<Estado, HashMap<Character, ArrayList<Estado>>>
			tmp = t.getTransitions();
		
		for(Estado s : tmp.keySet()){
			transitions.put(new Estado(s), new HashMap<>());
			for(char c : tmp.get(s).keySet())
				transitions.get(s).put(c, new ArrayList<>(tmp.get(s).get(c)));
		}
	}
	
	public ArrayList<Estado> obterProximosEstados(Estado current, char trigger){
		if(!transitions.containsKey(current) || 
				!transitions.get(current).containsKey(trigger))
			return new ArrayList<>();
		return transitions.get(current).get(trigger);
	}
	
	public void removerTransicaoPorSimbolo(char simbol){
		for(Estado key : transitions.keySet()){
			if(transitions.get(key).containsKey(simbol))
				transitions.get(key).remove(simbol);
		}
	}
	
	public void removerTransicaoPorEstado(Estado s){
		transitions.remove(s);
		for(Estado s2 : transitions.keySet())
			for(char c : transitions.get(s2).keySet())
				transitions.get(s2).get(c).remove(s);
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
		for(Estado key : transitions.keySet()){
			for(char c : transitions.get(key).keySet()){
				if(transitions.get(key).get(c).remove(sOld))
					transitions.get(key).get(c).add(sNew);
			}
		}
	}
	
	public HashMap<Estado, HashMap<Character, ArrayList<Estado>>> getTransitions(){
		return this.transitions;
	}

	@Override
	public String toString() {
		return "Transitions [transitions=" + transitions + "]";
	}
}
