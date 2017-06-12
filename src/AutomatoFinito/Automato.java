package AutomatoFinito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import Principal.Regular;

public class Automato extends Regular {
  private ArrayList<Estado> estados;
  private ArrayList<Character> alfabeto;
  private Transicoes transicoes;
  
  private Estado estadoInicial;
  
  private ArrayList<Estado> estadosFinais;
  
  public Automato(Automato automato){
	  this(   
			  new ArrayList<>(automato.estados), 
			  new ArrayList<>(automato.alfabeto),
			  new Transicoes(automato.transicoes),
			  new Estado(automato.estadoInicial),
			  new ArrayList<>(automato.estadosFinais),
			  automato.titulo, 
			  automato.descricao
	 );
  }

  public Automato(ArrayList<Character> alfabeto){
	  this(
			  new ArrayList<>(), 
			  new ArrayList<>(alfabeto), 
			  new Transicoes(), 
			  null, 
			  new ArrayList<>(), 
			  "", 
			  ""
	  );
  }

  public Automato(){
	  this(
			  new ArrayList<>(), 
			  new ArrayList<>(), 
			  new Transicoes(), 
			  null, 
			  new ArrayList<>(), 
			  "", 
			  ""
	  );
  }

	public Automato(ArrayList<Estado> estados, ArrayList<Character> alfabeto, 
			Transicoes transicoes, Estado estadoInicial, ArrayList<Estado> estadosFinais,
			String titulo, String descricao) {
		this.estados = estados;
		this.alfabeto = alfabeto;
		this.transicoes = transicoes;
		this.estadoInicial = estadoInicial;
		this.estadosFinais = estadosFinais;
		this.titulo = titulo;
		this.descricao = descricao;
	}
  
  
	public ArrayList<Estado> estados(){
		return this.estados;
	}
	
	public void estados(ArrayList<Estado> estados){
		this.estados = estados;
	}
	
	public Estado obterEstadoPor(String nome){
		for (Estado estado: this.estados){
			if (estado.nome() == nome){
				return estado;
			}
		}
		return null;
	}
	
	public void remove(Estado estado){
		this.transicoes.remover(estado);
		this.estados.remove(estado);
		this.estadosFinais.remove(estado);
		if (estado.equals(this.estadoInicial)){
			this.estadoInicial = null;
		}
	}
	
	public boolean pertence(Estado estado){
		return this.estados.contains(estado);
	}
	
	//TODO: maquiagem - ordenarEstados ordenarAlfabeto
	
	public ArrayList<Character> alfabeto(){
		return this.alfabeto;
	}
	
	public void alfabeto(ArrayList<Character> alfabeto){
		this.alfabeto = alfabeto;
	}
	
	public void adicionar(char simbolo){
		if (!this.alfabeto.contains(simbolo)) this.alfabeto.add(simbolo);
	}
	
	public void remover(char simbolo){
		if(this.alfabeto.contains(simbolo)){
			this.transicoes.remover(simbolo);
			this.alfabeto.remove((Character) simbolo);
		}
	}
	
	public Transicoes transicoes(){
		return this.transicoes;
	}
	
	public void adicionar(Transicoes transicoes){
		this.transicoes.adicionar(transicoes);
	}
	
	public void adicionar(Estado corrente, char simbolo, Estado proximo){
		if (!this.estados.contains(corrente) || !this.alfabeto.contains(simbolo)){
			return;
		}
		
		this.transicoes.adicionar(corrente,simbolo,proximo);
	}
	
	// TODO: getEpsilonFechos
	
	public HashMap<Estado, Set<Estado>> obterTodosOsEpsilons(){
		HashMap<Estado, Set<Estado>> mapa = new HashMap<>();
		Set<Estado> estados;
		for (Estado estado: this.estados){
			estados = new HashSet<>();
			this.obterEpsilon(estado, estados);
			mapa.put(estado,estados);
		}
		
		return mapa;
	}
	
	public ArrayList<Estado> alcancaveis(Estado corrente, char simbolo){
		if (!this.estados.contains(corrente) || !this.alfabeto.contains(simbolo)){
			return new ArrayList<>();
		}
		return this.transicoes.obterProximo(corrente, simbolo);
	}
	
	public HashMap<Estado, Set<Estado>> obterEstadosComEpsilon(){
		HashMap<Estado, Set<Estado>> mapa = new HashMap<>();
		Set<Estado> auxiliar;
		for (Estado estado: this.estados){
			auxiliar = new HashSet<>();
			this.obterEpsilon(estado, auxiliar);
		}
		
		return mapa;
	}
	
	private void obterEpsilon(Estado estado, Set<Estado> estados){
		estados.add(estado);
		for(Estado estado_auxiliar: this.proximosEstados(estado, '&')){
			if(!estado_auxiliar.equals(estado)){
				this.obterEpsilon(estado_auxiliar, estados);
			}
		}
	}
	
	public ArrayList<Estado> proximosEstados(Estado atual, char simbolo){
		if (!this.estados.contains(atual) || !alfabeto.contains(simbolo)){
			return new ArrayList<>();
		}
		
		return this.transicoes.obterProximo(atual, simbolo);
	}
	
	public ArrayList<Estado> todosProximosEstados(Estado atual){
		ArrayList<Estado> estados = new ArrayList<>();
		for (char simbolo: this.alfabeto){
			estados.addAll(this.transicoes.obterProximo(atual, simbolo));
		}
		return estados;
	}
	
	public ArrayList<Estado> obterTodosEstadosAnteriores(Estado atual){
		ArrayList<Estado> estados = new ArrayList<>();
		for (Estado estado : this.estados){
			if(this.todosProximosEstados(atual).contains(atual)){
				estados.add(estado);
			}
		}
		return estados;
	}
	
	public void transicao(Estado atual, Estado proximo){
		if (this.estados.contains(atual)){
			this.transicoes.trocarProximos(atual, proximo);
		}
	}
	
	public Estado estadoInicial(){
		return this.estadoInicial;
	}
	
	public void estadoInicial(Estado estado){
		this.estadoInicial = estado;
	}
	
	public ArrayList<Estado> estadosFinais() {
		return this.estadosFinais;
	}
	
	public void estadosFinais(ArrayList<Estado> estados){
		this.estadosFinais = estados;
	}
	
	public void adicionarEstadoFinal(Estado estado){
		if (this.estados.contains(estado) && !this.estadosFinais.contains(estado)){
			this.estadosFinais.add(estado);
		}
	}
	
	public void removerEstadoFinais(Estado estado){
		if (this.estados.contains(estado)  && this.estadosFinais.contains(estado)){
			this.estadosFinais.remove(estado);
		}
	}
	
	public boolean estadoFinal(Estado estado){
		return this.estadosFinais.contains(estado);
	}
	
	public ArrayList<Estado> obterEstadosNaoFinais(){
		ArrayList<Estado> estados = new ArrayList<>();
		for (Estado estado: this.estados){
			if (!this.estadosFinais.contains(estado)){
				estados.add(estado);
			}
		}
		return estados;
	}
	
	@Override
	public boolean ehautomato(){
		return true;
	}
	
	public Automato clone(boolean redefinirNomeDosEstados){
		Automato automato = new Automato(this.alfabeto);
		HashMap<Estado, Estado> mapa = new HashMap<>();
		Estado estado_temporario;
		
		for(Estado estado_auxiliar: this.estados){
			if (redefinirNomeDosEstados){
				estado_temporario = new Estado(estado_auxiliar+"'");
			} else {
				estado_temporario = new Estado(estado_auxiliar);
			}
			
			automato.estados.add(estado_auxiliar);
			if (this.estadosFinais.contains(estado_temporario)){
				automato.estadosFinais.add(estado_temporario);
			}
			
			if (this.estadoInicial.equals(estado_auxiliar)){
				automato.estadoInicial = estado_auxiliar;
			}
			mapa.put(estado_temporario, estado_auxiliar);
		}
		
		for (Estado estado: this.estados){
			for (char simbolo: this.alfabeto){
				for (Estado estado_auxiliar: this.proximosEstados(estado, simbolo)){
					automato.transicoes.adicionar(mapa.get(estado), simbolo, mapa.get(estado_auxiliar));
				}
			}
		}
		
		return automato;
	}
}
