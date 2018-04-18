package relatorio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.PieChartModel;

@ManagedBean(name = "relatorios")
@SessionScoped
public class Relatorios {

	
	public int tipoRelatorio;
	public int tipoGrafico;
	public Map<String, ArrayList<Integer>> dadosChart;
	public static String[] tipo = {"Pedidos totais do E-SIC", "Pedidos mensais do E-SIC", "Pedidos anuais do E-SIC", "Pedidos anuais acumulados do E-SIC",
									"Pedidos por �rg�o do E-SIC", "Pedidos por entidade do E-SIC", "Pedidos por assunto do E-SIC", "Pedidos por tipo de pessoa do E-SIC", 
									"Pedidos por estado do E-SIC"};
	public int[] metricas;
	public boolean dataBool;
	public boolean mesesBool;
	public boolean anosBool;
	public boolean orgaoBool;
	public boolean entidadeBool;
	public boolean estadosBool;
	public boolean assuntoBool;
	public boolean tipoPessoaBool;
	public Date periodoIni;
	public Date periodoFim;
	public String[] meses ;
	public String[] anosSelecionados;
	public String[] orgaos;
	public String[] entidades;
	public String[] assuntos;
	public String tipoPessoa;
	public String[] estado;
	public String tipoSolicitacao;
	public String tempo;
	public String status;
	public ArrayList<Integer> anos;
	
	
	
	@PostConstruct
	public void Relatorios() {
		anos = retornarListaAnosAteHoje();
	}
	
	/**
	 * 1 - pedidos totais do esic
	 * 2- pedidos mensais
	 * 3- pedidos anuais
	 * 4- pedidos acumulados
	 * 5 - pedidos por orgao
	 * 6 - pedidos por entidade
	 * 7- pedidos por assuntos
	 * 8 - pedidos por tipo de pessoa
	 * 9- uf
	 * @return 
	 */
	
	public Map<String, ArrayList<Integer>> redirecionarFiltroDados(long tipoDados) {
		
		switch ((int)tipoDados){
		case 1:
			dadosChart = FiltrarDadosRelatorioEstatico.gerarAcompanhamentoGeralDosPedidosInformacao();
			break;
		case 2:
			dadosChart = FiltrarDadosRelatorioEstatico.gerarAcompanhamentoMensalPedidoInformacao();
			break;
		case 3:
			dadosChart = FiltrarDadosRelatorioEstatico.gerarAcompanhamentoAnualPedidoInformacao();
			break;
		case 4:
			dadosChart = FiltrarDadosRelatorioEstatico.gerarAcompanhamentoAnualAcumuladoPedidoInformacao();
			break;
		case 5:
			dadosChart = FiltrarDadosRelatorioEstatico.gerarAcompanhamentoOrgaoPedidoInformacao();
			break;
		case 6:
			dadosChart = FiltrarDadosRelatorioEstatico.gerarAcompanhamentoEntidadePedidoInformacao();
			break;
		case 7:
			dadosChart = FiltrarDadosRelatorioEstatico.gerarAcompanhamentoAssuntoPedidoInformacao();
			break;
		case 8:
			dadosChart = FiltrarDadosRelatorioEstatico.gerarAcompanhamentoTipoPessoaGeneroPedidoInformacao();
			break;
		case 9:
			dadosChart = FiltrarDadosRelatorioEstatico.gerarAcompanhamentoEstadosPedidoInformacao();
			break;
		}
		
		return dadosChart;
	}
	
	public void desenhar(long tipoDados) {
		
		if(tipoDados == (long)7 || tipoDados == (long)8 || tipoDados == (long)9) {
			desenharPieChart( tipoDados);
		}else {
			desenharBarChart( tipoDados);
		}
	}
	
	public PieChartModel desenharPieChart( long tipoDados) {
		redirecionarFiltroDados(tipoDados);
		DrawPieChart modelPie = new DrawPieChart();
		return modelPie.createPieModel2(dadosChart, tipo[(int)tipoDados-1]);
	}
	
	
	public BarChartModel desenharBarChart( long tipoDados) {
		redirecionarFiltroDados(tipoDados);
		int valorMaior = identificarValorMaximoGrafico(dadosChart);
		DrawBarChart model = new DrawBarChart();
		return model.createBarModel(tipo[(int)tipoDados-1], dadosChart, tipoDados, valorMaior);
		
	}
	
	public int identificarValorMaximoGrafico(Map<String, ArrayList<Integer>> dadosChart) {
		int valorMaior = 0;
		for (String key : dadosChart.keySet()) {
			ArrayList<Integer> list = dadosChart.get(key);
			valorMaior = Collections.max(list,null) > valorMaior ? Collections.max(list,null) : valorMaior;
		}
		return (valorMaior+10);
	}
	
	public ArrayList<Integer> retornarListaAnosAteHoje() {
		ArrayList<Integer> anos = new ArrayList<>();
		Calendar a = Calendar.getInstance();
		for (int i = 2012; i <= a.get(Calendar.YEAR); i++) {
			anos.add(i);
		}
		return anos;
	}
	
	@SuppressWarnings("serial")
	public void verificarBool() {
		List<Integer> comparar = new ArrayList<Integer>() {
			{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
				add(6);
			}
		};

		if (metricas.length != 0) {
			for (int i : metricas) {
				comparar.remove(comparar.indexOf(i));
				switch (i) {
				case 1:
					setDataBool(true);
					break;
				case 2:
					setMesesBool(true);
					break;
				case 3:
					setAnosBool(true);
					break;
				case 4:
					setOrgaoBool(true);
					break;
				case 5:
					setEntidadeBool(true);
					break;
				case 6:
					setEstadosBool(true);
					break;
				case 7:
					setAssuntoBool(true);
					break;
				case 8:
					setTipoPessoaBool(true);
					break;
				}
			}

			for (int i : comparar) {
				switch (i) {
				case 1:
					setDataBool(false);
					break;
				case 2:
					setMesesBool(false);
					break;
				case 3:
					setAnosBool(false);
					break;
				case 4:
					setOrgaoBool(false);
					break;
				case 5:
					setEntidadeBool(false);
					break;
				case 6:
					setEstadosBool(false);
					break;
				case 7:
					setAssuntoBool(false);
					break;
				case 8:
					setTipoPessoaBool(false);
					break;
				}
			}
		} else {
			setAssuntoBool(false);
			setDataBool(false);
			setEntidadeBool(false);
			setEstadosBool(false);
			setOrgaoBool(false);
			setTipoPessoaBool(false);

		}
	}
	
	@SuppressWarnings("unused")
	public void botao() {
		Map<String, ArrayList<String>> dados = new HashMap<>();
		String[] keys = {"data", "meses", "anos","orgao", "entidade", "estados", "assunto", "tipoPesso"};
//		List<List<String>> variaveis = new ArrayList<List<String>>() {
//			{
//				add(Arrays.asList(new String[] {periodoIni.toString(), periodoFim.toString()}));
//				add(Arrays.asList(meses));
//				add(Arrays.asList(anosSelecionados));
//				add(Arrays.asList(orgaos));
//				add(Arrays.asList(entidades));
//				add(Arrays.asList(estado));
//				add(Arrays.asList(assuntos));
//				add(Arrays.asList(new String[] {tipoPessoa}));
//			}
//		};
//		
		ArrayList<String> a = new ArrayList<>();
		for (String item : anosSelecionados) {
			a.add(item);
		}
		for (int itemEscolhido : metricas) {
			dados.put(keys[itemEscolhido-1], a);
		}

		
		String retorno = metodo(tipoSolicitacao, tempo, status, dados);
	}
	
	
	public String metodo(String tipoSolicitacao, String tempo, String status, Map<String, ArrayList<String>> dados) {
		String query = "SELECT idSolicitacao FROM Solicitacao as slt";
		if(dados.containsKey("tipoPessoa") || dados.containsKey("estados") ) {
			query = query.concat(" JOIN slt.cidadao as cidadao WHERE slt.cidadao.idCidadao = cidadao.idCidadao "
						+ "AND slt.tipo = '" + tipoSolicitacao + "' AND slt.status = '" + status+"'");
		}else {
			query = query.concat(" WHERE slt.tipo = '" + tipoSolicitacao + "' AND slt.status = '" + status+"'");
		}
			
		ArrayList<String> base = new ArrayList<>();
		if (dados.containsKey(tempo)) {
			if (!dados.get(tempo).get(0).equals("Todos")) {
				query = query.concat(" AND ");
				ArrayList<String> keysValues = dados.get(tempo);
				if (tempo.equals("meses")) {
					for (String t : keysValues) {
						base.add(t);
						query = query.concat("dataIni LIKE '%-" + t + "-%'");
						if (!(keysValues.indexOf(t) == (keysValues.size() - 1))) {
							query = query.concat(" OR ");
						}
					}
				} else {
					for (String t : keysValues) {
						base.add(t);
						query = query.concat("dataIni LIKE '" + t + "%'");
						if (!(keysValues.indexOf(t) == (keysValues.size() - 1))) {
							query = query.concat(" OR ");
						}
					}
				}
			}
			dados.remove(tempo);
		}
		
		for ( String key : dados.keySet()) {
			switch (key) {
			case "data":
				query = query.concat(" AND slt.dataIni >= '"+dados.get(key).get(0)+"' AND dataIni <= '"+dados.get(key).get(1)+"'");
				break;
			case "meses":
				query = query.concat(" AND ");
				for(int i = 0 ; i <dados.get(key).size(); i++) {
					if(i != 0) {
						query = query.concat(" OR slt.dataIni LIKE '%-"+dados.get(key).get(i)+"-%'");
					}else {
						query = query.concat(" slt.dataIni LIKE '%-"+dados.get(key).get(i)+"-%'");
					}
				}
				break;
			case "anos":
				query = query.concat(" AND ");
				for(int i = 0 ; i <dados.get(key).size(); i++) {
					if(i != 0) {
						query = query.concat(" OR slt.dataIni LIKE '"+dados.get(key).get(i)+"-%'");
					}else {
						query = query.concat(" slt.dataIni LIKE '"+dados.get(key).get(i)+"-%'");
					}
				}
				break;
			case "orgao" :
				query = query.concat(" AND ");
				for(int i = 0 ; i <dados.get(key).size(); i++) {
					if(i != 0) {
						query = query.concat(" OR slt.entidades.idEntidades = '"+dados.get(key).get(i)+"'");
					}else {
						query = query.concat(" slt.entidades.idEntidades = '"+dados.get(key).get(i)+"'");
					}
				}
				break;
			case "entidade" :
				query = query.concat(" AND ");
				for(int i = 0 ; i <dados.get(key).size(); i++) {
					if(i != 0) {
						query = query.concat(" OR slt.entidades.idEntidades = '"+dados.get(key).get(i)+"'");
					}else {
						query = query.concat(" slt.entidades.idEntidades = '"+dados.get(key).get(i)+"'");
					}
				}
				break;
			case "estados" :
				query = query.concat(" AND ");
				for(int i = 0 ; i <dados.get(key).size(); i++) {
					if(i != 0) {
						query = query.concat(" OR cidadao.estado = '"+dados.get(key).get(i)+"'");
					}else {
						query = query.concat(" cidadao.estado = '"+dados.get(key).get(i)+"'");
					}
				}
				break;
				
			case "tipoPessoa" :
//				query = query.concat(" AND ");
//				for(int i = 0 ; i <dados.get(key).size(); i++) {
//					if(i == 0) {
//						query = query.concat(" OR cidadao.estado = '"+dados.get(key).get(i)+"'");
//					}else {
//						query = query.concat(" cidadao.estado = '"+dados.get(key).get(i)+"'");
//					}
//				}
				break;
				
			case "assunto" :
				query = query.concat(" AND ");
				for(int i = 0 ; i <dados.get(key).size(); i++) {
					if(i == 0) {
						query = query.concat(" OR slt.acoes.idAcoes = '"+dados.get(key).get(i)+"'");
					}else {
						query = query.concat(" slt.acoes.idAcoes = '"+dados.get(key).get(i)+"'");
					}
				}
				break;

			default:
				break;
			}
		}
		return query;

	}
	
	
	public int teste1(int a, int b) {
		return a + b;
	}
	
	public int teste1(int a) {
		return a+1;
	}

	public int getTipoRelatorio() {
		return tipoRelatorio;
	}
	public void setTipoRelatorio(int tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}
	public int getTipoGrafico() {
		return tipoGrafico;
	}
	public void setTipoGrafico(int tipoGrafico) {
		this.tipoGrafico = tipoGrafico;
	}

	public boolean isDataBool() {
		return dataBool;
	}

	public void setDataBool(boolean newData) {
			dataBool = newData;
	}
	
	public boolean isMesesBool() {
		return mesesBool;
	}

	public void setMesesBool(boolean mesesBool) {
		this.mesesBool = mesesBool;
	}

	public boolean isAnosBool() {
		return anosBool;
	}

	public void setAnosBool(boolean anosBool) {
		this.anosBool = anosBool;
	}

	public boolean isOrgaoBool() {
		return orgaoBool;
	}

	public void setOrgaoBool(boolean newOrgao) {
			orgaoBool = newOrgao;
	}

	public boolean isEntidadeBool() {
		return entidadeBool;
	}

	public void setEntidadeBool(boolean newEntidade) {
		entidadeBool = newEntidade;
		
	}

	public boolean isEstadosBool() {
		return estadosBool;
	}

	public void setEstadosBool(boolean newEstado) {
			estadosBool = newEstado;
	}

	public boolean isAssuntoBool() {
		return assuntoBool;
	}

	public void setAssuntoBool(boolean newAss) {
		assuntoBool = newAss;
	}

	public boolean isTipoPessoaBool() {
		return tipoPessoaBool;
	}

	public void setTipoPessoaBool(boolean newTipo) {
			tipoPessoaBool = newTipo;
	}

	public int[] getMetricas() {
		return metricas;
	}

	public void setMetricas(int[] metricas) {
		this.metricas = metricas;
	}

	public Date getPeriodoIni() {
		return periodoIni;
	}

	public void setPeriodoIni(Date periodoIni) {
		this.periodoIni = periodoIni;
	}

	public Date getPeriodoFim() {
		return periodoFim;
	}

	public void setPeriodoFim(Date periodoFim) {
		this.periodoFim = periodoFim;
	}

	public String[] getMeses() {
		return meses;
	}

	public void setMeses(String[] meses) {
		this.meses = meses;
	}

	public String[] getOrgaos() {
		return orgaos;
	}

	public void setOrgaos(String[] orgaos) {
		this.orgaos = orgaos;
	}

	public String[] getEntidades() {
		return entidades;
	}

	public void setEntidades(String[] entidades) {
		this.entidades = entidades;
	}

	public String[] getAssuntos() {
		return assuntos;
	}

	public void setAssuntos(String[] assuntos) {
		this.assuntos = assuntos;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String[] getEstado() {
		return estado;
	}

	public void setEstado(String[] estado) {
		this.estado = estado;
	}

	public String  getTipoSolicitacao() {
		return tipoSolicitacao;
	}

	public void setTipoSolicitacao(String  tipoSolicitacao) {
		this.tipoSolicitacao = tipoSolicitacao;
	}

	public String  getTempo() {
		return tempo;
	}

	public void setTempo(String  tempo) {
		this.tempo = tempo;
	}

	public String  getStatus() {
		return status;
	}

	public void setStatus(String  status) {
		this.status = status;
	}

	public ArrayList<Integer> getAnos() {
		return anos;
	}

	public void setAnos(ArrayList<Integer> anos) {
		this.anos = anos;
	}

	public String[] getAnosSelecionados() {
		return anosSelecionados;
	}

	public void setAnosSelecionados(String[] anosSelecionados) {
		this.anosSelecionados = anosSelecionados;
	}

	

	
}
