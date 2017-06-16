package principal;

/**
 * Exception criada para ser usada na classe Main para
 * passar informações do controle para a interface
 * que cuida de adicionar/editar as Grs/Ers
 * 
 * @author Maurilio Atila
 * @author Wilian Kraemmer
 * 
 * 
 */
@SuppressWarnings("serial")
public class WarningException extends Exception {
	
	public WarningException(String msg) {
		super(msg);
	}
}
