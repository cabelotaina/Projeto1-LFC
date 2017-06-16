package db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import expressao_regular.ExpressaoRegular;
import expressao_regular.ControleER;
import gramatica.Gramatica;
import principal.Regular;
import gramatica.ControleGR;

public class RegularDao extends Dao {
	
	public RegularDao() {
		super();
	}
	
	public void addRegular(Regular r) throws Exception {
		if(r.ehAutomato()){
			System.out.println("RegularDao > Soh eh possivel adicionar GRs ou ERs!");
			return;
		}else if(r.isDumbGrEr())//intersecções não vão ser salvas no DB
			return;
		
		HashMap<String, String> dados = new HashMap<>();
		dados.put("titulo", r.titulo());
		if(r.ehGramatica())
			dados.put("gr_er", ((Gramatica)r).getGrammar());
		else
			dados.put("gr_er", ((ExpressaoRegular)r).getRegEx());
		dados.put("extras", r.extras());
		
		this.insert(dados);
	}
	
	public void editRegular(Regular r) throws Exception {
		if(r.ehAutomato()){
			System.out.println("RegularDao > Soh eh possivel adicionar GRs ou ERs!");
			return;
		}else if(r.isDumbGrEr())//intersecções não vão ser salvas no DB
			return;
		
		HashMap<String, String> dados = new HashMap<>();
		if(r.ehGramatica())
			dados.put("gr_er", ((Gramatica)r).getGrammar());
		else
			dados.put("gr_er", ((ExpressaoRegular)r).getRegEx());
		dados.put("extras", r.extras());
		
		HashMap<String, String> where = new HashMap<>();
		where.put("titulo", r.titulo());
		
		this.update(dados, where);
	}
	
	public void setExtras(Regular r) throws Exception {
		if(r.ehAutomato()){
			System.out.println("RegularDao > Soh eh possivel adicionar GRs ou ERs!");
			return;
		}else if(r.isDumbGrEr())//intersecções não vão ser salvas no DB
			return;
		
		HashMap<String, String> dados = new HashMap<>();
		dados.put("extras", r.extras());
		
		HashMap<String, String> where = new HashMap<>();
		where.put("titulo", r.titulo());
		
		this.update(dados, where);
	}
	
	public void removeRegular(Regular r) throws Exception {
		if(r.ehAutomato()){
			System.out.println("RegularDao > Soh eh possivel adicionar GRs ou ERs!");
			return;
		}else if(r.isDumbGrEr())//intersecções não vão ser salvas no DB
			return;
		
		HashMap<String, String> where = new HashMap<>();
		where.put("titulo", r.titulo());
		
		this.delete(where);
	}
	
	public ArrayList<Regular> getAll() throws Exception {
		this.select("");
		ResultSet result = this.getResultSet();
		ArrayList<Regular> regResult = new ArrayList<>();
		String tmpTitulo;
		Regular tmpReg;
		
		while(result != null && result.next()){
			tmpTitulo = result.getString("titulo");
			if(tmpTitulo.contains("GR:")){
				tmpReg = ControleGR.createRegGrammar(tmpTitulo, result.getString("gr_er"));
				tmpReg.extras(result.getString("extras"));
				regResult.add(tmpReg);
			}else{
				tmpReg = ControleER.createRegExpression(tmpTitulo, result.getString("gr_er"));
				tmpReg.extras(result.getString("extras"));
				regResult.add(tmpReg);
			}
		}
		
		return regResult;
	}
}
