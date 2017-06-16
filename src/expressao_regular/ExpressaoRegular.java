package expressao_regular;

import principal.Regular;

public class ExpressaoRegular extends Regular {
	
	private String expressao_regular;
	
	public ExpressaoRegular(String expressao_regular, String titulo) {
		super();
		this.expressao_regular = expressao_regular;
		titulo(titulo);
	}
	
	/* Expressao Regular */
	public String obterExpressaoRegular() {
		return expressao_regular;
	}

	public void definirExpressaoRegular(String expressao_regular) {
		this.expressao_regular = expressao_regular;
	}
	
	/* Regular */
	@Override
	public boolean ehExpressaoRegular() {
		return true;
	}
	
	@Override
	public boolean isDumbGrEr(){
		return expressao_regular.equals("");
	}
	
	@Override
	public String toString() {
		return expressao_regular;
	}
	
}
