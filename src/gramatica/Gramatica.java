package gramatica;

import java.util.ArrayList;

import principal.Regular;

public class Gramatica extends Regular {
	
	private ArrayList<String> Vn;
	private ArrayList<Character> Vt;
	private ArrayList<Producao> producoes;
	private String simboloInicial;

	private String string_gramatica; //representacao em String da gramatica

	public Gramatica(String titulo) {
		this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 
				null, titulo, "");
	}
	
	public Gramatica(String titulo, String string_gramatica) {
		this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 
				null, titulo, string_gramatica);
	}
		
	public Gramatica(ArrayList<String> vn, ArrayList<Character> vt,
			ArrayList<Producao> producoes, String simboloInicial, String titulo, String string_gramatica) {
		super();
		Vn = vn;
		Vt = vt;
		this.producoes = producoes;
		this.simboloInicial = simboloInicial;
		this.string_gramatica = string_gramatica;
		titulo(titulo);
	}

	/* Vn */
	public ArrayList<String> obterVn() {
		return Vn;
	}

	public void definirVn(ArrayList<String> vn) {
		Vn = vn;
	}
	
	public void adicionarVn(String vn){
		if(!this.Vn.contains(vn))
			this.Vn.add(vn);
	}
	
	/* Vt */
	public ArrayList<Character> obterVt() {
		return Vt;
	}

	public void definirVt(ArrayList<Character> vt) {
		Vt = vt;
	}
	
	public void adicionarVt(char vt){
		if(!this.Vt.contains(vt))
			this.Vt.add(vt);
	}

	/* Productions */
	public ArrayList<Producao> obterProducoes() {
		return producoes;
	}
	
	public ArrayList<Producao> obterProducoes(String simboloAtual){
		ArrayList<Producao> producoes = new ArrayList<>();
		for(Producao p : this.producoes){
			if(p.obterAtual().equals(simboloAtual))
				producoes.add(p);
		}
		return producoes;
	}

	public void setProductions(ArrayList<Producao> producoes) {
		this.producoes = producoes;
	}
	
	public void adicionarProducao(String atual, char simbolo, String proximo){
		Producao p = new Producao(atual, simbolo, proximo);
		if(!this.producoes.contains(p))
			this.producoes.add(p);
	}
	
	public void adicionarProducao(Producao producao){
		if(!this.producoes.contains(producao))
			this.producoes.add(producao);
	}

	/* Simbolo Inicial */
	public String obterSimboloInicial() {
		return simboloInicial;
	}

	public void definirSimboloInicial(String simboloInicial) {
		this.simboloInicial = simboloInicial;
	}
	
	/* Grammar */
	public String obterGramatica() {
		return string_gramatica;
	}
	
	public void definirGramatica(String gramatica){
		this.string_gramatica = gramatica;
	}
	
	/* Regular */
	@Override
	public boolean ehGramatica() {
		return true;
	}
	
	@Override
	public boolean isDumbGrEr(){
		return string_gramatica.equals("");
	}
	
	@Override
	public String toString() {
		return string_gramatica;
	}
}
