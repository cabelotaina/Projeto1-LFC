package banco_de_dados;

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
	
	public void adicionarRegular(Regular r) throws Exception {
		if(r.ehAutomato()){
			System.out.println("RegularDao > Soh eh possivel adicionar GRs ou ERs!");
			return;
		}else if(r.isDumbGrEr())//intersecções não vão ser salvas no DB
			return;
		
		HashMap<String, String> dados = new HashMap<>();
		dados.put("titulo", r.titulo());
		if(r.ehGramatica())
			dados.put("gr_er", ((Gramatica)r).obterGramatica());
		else
			dados.put("gr_er", ((ExpressaoRegular)r).obterExpressaoRegular());
		dados.put("extras", r.extras());
		
		this.insert(dados);
	}
	
	public void editarRegular(Regular r) throws Exception {
		if(r.ehAutomato()){
			System.out.println("RegularDao > Soh eh possivel adicionar GRs ou ERs!");
			return;
		}else if(r.isDumbGrEr())//intersecções não vão ser salvas no DB
			return;
		
		HashMap<String, String> dados = new HashMap<>();
		if(r.ehGramatica())
			dados.put("gr_er", ((Gramatica)r).obterGramatica());
		else
			dados.put("gr_er", ((ExpressaoRegular)r).obterExpressaoRegular());
		dados.put("extras", r.extras());
		
		HashMap<String, String> where = new HashMap<>();
		where.put("titulo", r.titulo());
		
		this.update(dados, where);
	}
	
	public void definirExtras(Regular r) throws Exception {
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
		
		HashMap<String, String> onde = new HashMap<>();
		onde.put("titulo", r.titulo());
		
		this.delete(onde);
	}
	
	public ArrayList<Regular> obterTudo() throws Exception {
		this.select("");
		ResultSet resultado = this.getResultSet();
		ArrayList<Regular> regResult = new ArrayList<>();
		String tmpTitulo;
		Regular tmpReg;
		
		while(resultado != null && resultado.next()){
			tmpTitulo = resultado.getString("titulo");
			if(tmpTitulo.contains("GR:")){
				tmpReg = ControleGR.definirGramatica(tmpTitulo, resultado.getString("gr_er"));
				tmpReg.extras(resultado.getString("extras"));
				regResult.add(tmpReg);
			}else{
				tmpReg = ControleER.criarExpressaoRegular(tmpTitulo, resultado.getString("gr_er"));
				tmpReg.extras(resultado.getString("extras"));
				regResult.add(tmpReg);
			}
		}
		
		return regResult;
	}
}
