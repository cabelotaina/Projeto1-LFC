package gramatica;

import java.util.ArrayList;

import principal.Regular;

public class Gramatica extends Regular {
	
	private ArrayList<String> Vn;
	private ArrayList<Character> Vt;
	private ArrayList<Producao> productions;
	private String initialSimbol;

	private String grammar; //representacao em String da gramatica

	public Gramatica(String titulo) {
		this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 
				null, titulo, "");
	}
	
	public Gramatica(String titulo, String grammar) {
		this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 
				null, titulo, grammar);
	}
		
	public Gramatica(ArrayList<String> vn, ArrayList<Character> vt,
			ArrayList<Producao> productions, String initialSimbol, String titulo, String grammar) {
		super();
		Vn = vn;
		Vt = vt;
		this.productions = productions;
		this.initialSimbol = initialSimbol;
		this.grammar = grammar;
		titulo(titulo);
	}

	/* Vn */
	public ArrayList<String> getVn() {
		return Vn;
	}

	public void setVn(ArrayList<String> vn) {
		Vn = vn;
	}
	
	public void addVn(String vn){
		if(!this.Vn.contains(vn))
			this.Vn.add(vn);
	}
	
	/* Vt */
	public ArrayList<Character> getVt() {
		return Vt;
	}

	public void setVt(ArrayList<Character> vt) {
		Vt = vt;
	}
	
	public void addVt(char vt){
		if(!this.Vt.contains(vt))
			this.Vt.add(vt);
	}

	/* Productions */
	public ArrayList<Producao> getProductions() {
		return productions;
	}
	
	public ArrayList<Producao> getProductions(String currentSimbol){
		ArrayList<Producao> prods = new ArrayList<>();
		for(Producao p : productions){
			if(p.getCurrent().equals(currentSimbol))
				prods.add(p);
		}
		return prods;
	}

	public void setProductions(ArrayList<Producao> productions) {
		this.productions = productions;
	}
	
	public void addProduction(String current, char generated, String next){
		Producao p = new Producao(current, generated, next);
		if(!this.productions.contains(p))
			this.productions.add(p);
	}
	
	public void addProduction(Producao p){
		if(!this.productions.contains(p))
			this.productions.add(p);
	}

	/* Initial Simbol */
	public String getInitialSimbol() {
		return initialSimbol;
	}

	public void setInitialSimbol(String initialSimbol) {
		this.initialSimbol = initialSimbol;
	}
	
	/* Grammar */
	public String getGrammar() {
		return grammar;
	}
	
	public void setGrammar(String grammar){
		this.grammar = grammar;
	}
	
	/* Regular */
	@Override
	public boolean ehGramatica() {
		return true;
	}
	
	@Override
	public boolean isDumbGrEr(){
		return grammar.equals("");
	}
	
	@Override
	public String toString() {
		return grammar;
	}
}
