package principal;

/**
 * Classe usada como base para as Grs/Ers/Afs.
 * 
 * @author Maurilio Atila
 * @author Wilian Kraemmer
 *
 */
public abstract class Regular {
	
	// titulo da gr/er/af
	private String titulo;
	// operacoes ja executadas pelo usuario
	// AF, AFD, AFD_Min, AFD_Comp, Complete
	// e tambem se eh um AF da linguagem vazia.
	private String extras;
	
	public Regular(){
		this.extras = "";
	}
	
	// Titulo
	public String titulo(){
		return this.titulo;
	}
	
	public void titulo(String titulo){
		this.titulo = titulo;
	}
	
	// Extras
	public String extras(){
		return this.extras;
	}
	
	public void extras(String extras){
		this.extras = extras;
	}
	
	public void extra(String extra){	
		this.extras = extras.equals("") ?
				extra : extras+("|"+extra);
	}
	
	// Outras funcoes
	public boolean ehGramatica(){
		return false;
	}
	
	public boolean ehExpressaoRegular(){
		return false;
	}
	
	public boolean ehAutomato(){
		return false;
	}
	
	public boolean isDumbGrEr(){
		return false;
	}
}
