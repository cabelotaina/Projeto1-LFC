package banco_de_dados;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import automato.Automato;
import automato.ControleAF;
import expressao_regular.ControleER;
import expressao_regular.ExpressaoRegular;
import principal.Regular;

public class RegularDao extends Dao {

	public RegularDao() {
		super();
	}

	public void adicionarRegular(Regular r) throws Exception {

		HashMap<String, String> dados = new HashMap<>();
		dados.put("titulo", r.titulo());
		if (r.ehAutomato()) {
			dados.put("gr_er", ((Automato) r).transicoesString());
		} else
			dados.put("gr_er", ((ExpressaoRegular) r).obterExpressaoRegular());
		dados.put("extras", r.extras());

		this.insert(dados);
	}

	public void editarRegular(Regular r) throws Exception {

		if (r.isDumbGrEr())
			return;

		HashMap<String, String> dados = new HashMap<>();
		if (r.ehAutomato())
			dados.put("gr_er", ((Automato) r).transicoesString());
		else
			dados.put("gr_er", ((ExpressaoRegular) r).obterExpressaoRegular());
		dados.put("extras", r.extras());

		HashMap<String, String> where = new HashMap<>();
		where.put("titulo", r.titulo());

		this.update(dados, where);
	}

	public void definirExtras(Regular r) throws Exception {

		if (r.isDumbGrEr())
			return;

		HashMap<String, String> dados = new HashMap<>();
		dados.put("extras", r.extras());

		HashMap<String, String> where = new HashMap<>();
		where.put("titulo", r.titulo());

		this.update(dados, where);
	}

	public void removeRegular(Regular r) throws Exception {

		HashMap<String, String> where = new HashMap<>();
		where.put("titulo", r.titulo());
		this.delete(where);
	}

	public void removeTudo() throws Exception {
		String SQL = "DELETE FROM " + this.tabela;
		this.query(SQL);
		this.psmt.execute();
	}

	// Obter Tudo!!!!

	public ArrayList<Regular> obterTudo() throws Exception {
		this.select("");
		ResultSet resultado = this.getResultSet();
		ArrayList<Regular> regResult = new ArrayList<>();
		String tmpTitulo;
		Regular tmpReg;

		while (resultado != null && resultado.next()) {
			tmpTitulo = resultado.getString("titulo");
			if (tmpTitulo.contains("ER:")) {
				tmpReg = ControleER.criarExpressaoRegular(tmpTitulo, resultado.getString("gr_er"));
				tmpReg.extras(resultado.getString("extras"));
				regResult.add(tmpReg);
			} else {
				tmpReg = ControleAF.definirAutomato(tmpTitulo, resultado.getString("gr_er"));
				tmpReg.extras(resultado.getString("extras"));
				regResult.add(tmpReg);
			}
		}

		return regResult;
	}
}
