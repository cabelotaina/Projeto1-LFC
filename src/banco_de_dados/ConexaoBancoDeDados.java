package banco_de_dados;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexaoBancoDeDados {
	private static Connection CON;
	
	public static Connection obterConexao(){
		if(CON == null){
			try{
				Class.forName("org.sqlite.JDBC");
				CON = DriverManager.getConnection( "jdbc:sqlite:C:\\Users\\35223861833\\Documents\\GitHub\\Projeto1-LFC\\lib\\DataBase.db" );
			}catch( Exception e ){
				System.out.println( e );
			}
		}
		return CON;
	}
}
