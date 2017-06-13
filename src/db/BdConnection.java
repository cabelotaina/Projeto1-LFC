package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class BdConnection {
	private static Connection CON;
	private static final String file = ":resource:DataBase.db"; //"lib\\DataBase.db";
	
	public static Connection getConnection(){
		if(CON == null){
			try{
				Class.forName("org.sqlite.JDBC");
				CON = DriverManager.getConnection( "jdbc:sqlite:" +file );
			}catch( Exception e ){
				System.out.println( e );
			}
		}
		return CON;
	}
}
