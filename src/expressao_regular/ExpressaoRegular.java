package expressao_regular;

import principal.Regular;

public class ExpressaoRegular extends Regular {
	
	private String regEx;
	
	public ExpressaoRegular(String regEx, String titulo) {
		super();
		this.regEx = regEx;
		titulo(titulo);
	}
	
	/* RegEx */
	public String getRegEx() {
		return regEx;
	}

	public void setRegEx(String regEx) {
		this.regEx = regEx;
	}
	
	/* Regular */
	@Override
	public boolean ehExpressaoRegular() {
		return true;
	}
	
	@Override
	public boolean isDumbGrEr(){
		return regEx.equals("");
	}
	
	@Override
	public String toString() {
		return regEx;
	}
	
}
