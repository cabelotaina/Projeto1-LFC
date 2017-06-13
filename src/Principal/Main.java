package Principal;

import AutomatoFinito.Automato;
import DeSimone.DeSimone;

public class Main {

	public static void main(String[] args) {
	  Automato automato = DeSimone.criarAutomato("(a.b|a.c)*.a?.(b.a?.c)*");
	  System.out.println(automato.estados());
	  System.out.println(automato.titulo);

	}

}
