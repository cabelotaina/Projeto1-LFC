package banco_de_dados;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexaoBancoDeDados {
	private static Connection CON;
	
	public static Connection obterConexao(){
		if(CON == null){
			try{
				String banco = "jdbc:sqlite:lib/DataBase.db";
				Class.forName("org.sqlite.JDBC");
				CON = DriverManager.getConnection( banco );
			}catch( Exception e ){
				System.out.println( e );
			}
		}
		return CON;
	}
}
