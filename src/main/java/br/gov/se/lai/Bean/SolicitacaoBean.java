package br.gov.se.lai.Bean;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import br.gov.se.lai.DAO.AcoesDAO;
import br.gov.se.lai.DAO.EntidadesDAO;
import br.gov.se.lai.DAO.MensagemDAO;
import br.gov.se.lai.DAO.SolicitacaoDAO;
import br.gov.se.lai.entity.Cidadao;
import br.gov.se.lai.entity.Entidades;
import br.gov.se.lai.entity.Mensagem;
import br.gov.se.lai.entity.Solicitacao;
import br.gov.se.lai.utils.HibernateUtil;



@ManagedBean(name = "solicitacao")
@SessionScoped
public class SolicitacaoBean implements Serializable{
	

	private static final long serialVersionUID = -9191715805520708190L;
	private Solicitacao solicitacao;
	private List<Solicitacao> solicitacoes;
	private List<Solicitacao> filteredSolicitacoes;
	private Cidadao cidadao;;
	private List<Entidades> entidades;
	private Calendar datainic;
	private String status;
	private Calendar datafim;
	private int idEntidades;
	private int idAcao;
	private int idSolicitacao;
	private Mensagem mensagem;	
	private boolean ativa;
	private final static int constanteTempo = 10;

	@PostConstruct
	public void init() {
		this.solicitacao = new Solicitacao();
		this.mensagem = new Mensagem();
		this.cidadao = new Cidadao();
		this.entidades = new ArrayList<Entidades>(EntidadesDAO.list());
	}
	

	public String save() {
		UsuarioBean usuarioBean = (UsuarioBean) HibernateUtil.RecuperarDaSessao("usuario");		
		List<Cidadao> listCidadao = new ArrayList<Cidadao>(usuarioBean.getUsuario().getCidadaos());	
		
		//Salvar Solicita��o
		this.solicitacao.setCidadao(listCidadao.get(0));	
		this.solicitacao.setAcoes(AcoesDAO.findAcoes(idAcao));
		this.solicitacao.setDataIni(new Date(System.currentTimeMillis()));	
		this.solicitacao.setDataLimite(java.sql.Date.valueOf(LocalDate.now().plusDays(20)));
		this.solicitacao.setStatus("Aberta");
		SolicitacaoDAO.saveOrUpdate(solicitacao);
		
		//Salvar Mensagem
		this.mensagem.setUsuario(usuarioBean.getUsuario()); 
		this.mensagem.setData(java.sql.Date.valueOf(LocalDate.now()));	
		this.mensagem.setSolicitacao(solicitacao);
		this.mensagem.setTipo((short)1);
		MensagemDAO.saveOrUpdate(mensagem);
		
		return "/index";
	}	
	public void finalizarSolicitacao() {
		this.solicitacao = SolicitacaoDAO.findSolicitacao(idSolicitacao);
		this.solicitacao.setStatus("Finalizada");
		this.solicitacao.setDatafim(new Date(System.currentTimeMillis()));
		SolicitacaoDAO.saveOrUpdate(solicitacao);
	}
	
	public String verificaCidadaoSolicitacao() {
		UsuarioBean usuarioBean = (UsuarioBean) HibernateUtil.RecuperarDaSessao("usuario");
		List<Cidadao> listCidadao = new ArrayList<Cidadao>(usuarioBean.getUsuario().getCidadaos());	
		
		if(usuarioBean.getUsuario().getPerfil() == 0) {
			//se n�o tiver cadastro de usuario, vai cadastrar primeiro
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Us�rio inv�lido.", "Realize cadastro."));
			usuarioBean.setVeioDeSolicitacao(1);
			return "pages/cad_usuario";
		} else {
				if (usuarioBean.getUsuario().getPerfil() == 1 || usuarioBean.getUsuario().getPerfil() != 2) {
					//verifico se h� a instancia de um usuario e se este usuario n�o � um respons�vel
					
					if ((listCidadao.isEmpty()) && (usuarioBean.getUsuario().getPerfil() == 1)) {
						//se tiver cadastro de usuario mas n�o tiver de cidad�o, primeiro precisa cadastrar cidad�o
						return "pages/cad_cidadao";
					} else {
						//se j� for cadastrado usuario e cidad�o inicia solicitacao
						return "pages/questionario1";
					}
				} else {
					//Se for um respons�vel n�o tem autoriza��o para solicitar
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Usu�rio sem permiss�o.", "Tente outro login."));
					return null;
				}
			}	
	}
	
	public String verificaCidadaoConsulta() {
		UsuarioBean usuarioBean = (UsuarioBean) HibernateUtil.RecuperarDaSessao("usuario");
		if(usuarioBean.getUsuario().getPerfil() == 3) {
			return "pages/consulta";
		}else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Uus�rio sem permiss�o.", "Tente outro login."));
				return null;
			}
	}	

	public void listPersonalizada(AjaxBehaviorEvent e){
		if(status == "Todas") {
			filteredSolicitacoes = SolicitacaoDAO.list();
		}else {
			filteredSolicitacoes = SolicitacaoDAO.listPersonalizada(status);
		}
	}

	
	public void verificaTempoSolicitacao() {
		Date now = new Date();
		for (Solicitacao solicitacao : filteredSolicitacoes) {
			if(solicitacao.getDataLimite()!=null && !solicitacao.getStatus().equals("Finalizada")) {
					if(now.after(solicitacao.getDataLimite())) {
						if (solicitacao.getMensagems().size() == 1) {
							setStatus("Negada");
							prorrogar(solicitacao);
							SolicitacaoDAO.saveOrUpdate(solicitacao);
						}else {
							solicitacao.setStatus("Finalizada");
							solicitacao.setDatafim(new Date(System.currentTimeMillis()));
							SolicitacaoDAO.saveOrUpdate(solicitacao);
						}
					}
			}
		} 
	} 
	
	public static int prazoResposta(String status) {
		switch (status) {
		case "Aberta" :
			return constanteTempo;
		case "Prorrogada" :
			return constanteTempo;
		case "Negada" :
			return constanteTempo;
		case "Recurso" :
			return 5;
		default:
			return 20;
		}
	}
	
	
	public void prorrogar(Solicitacao solicitacao) {
		if(solicitacao.getStatus() != "Prorrogada") {
			solicitacao.setStatus(status);
			solicitacao.setDataLimite(java.sql.Date.valueOf(Instant.ofEpochMilli(solicitacao.getDataLimite().getTime()).atZone(ZoneId.systemDefault()).toLocalDate().plusDays(prazoResposta(solicitacao.getStatus()))));
			SolicitacaoDAO.saveOrUpdate(solicitacao);
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "J� est� em per�odo de prorroga��o.",null));
		}
	}
	
	public void recurso() {
		prorrogar(solicitacao);
	}
	

//GETTERS E SETTERS ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
	
	
	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public List<Entidades> getEntidades() {
		return entidades;
	}

	public void setEntidades(List<Entidades> entidades) {
		this.entidades = entidades;
	}

	public Calendar getDatainic() {
		return datainic;
	}

	public void setDatainic(Calendar date) {
		this.datainic = date;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Calendar getDatafim() {
		return datafim;
	}

	public void setDatafim(Calendar datafim) {
		this.datafim = datafim;
	}

	public int getIdEntidades() {
		return idEntidades;
	}

	public void setIdEntidades(int idEntidades) {
		this.idEntidades = idEntidades;
	}

	public Cidadao getCidadao() {
		return cidadao;
	}

	public void setCidadao(Cidadao cidadao) {
		this.cidadao = cidadao;
	}


	public Mensagem getMensagem() {
		return mensagem;
	}


	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}


	public List<Solicitacao> getSolicitacoes() {
		return SolicitacaoDAO.list();
	}


	@SuppressWarnings("unchecked")
	public Set<Mensagem> getMensagems() {
		return (Set<Mensagem>) MensagemDAO.list(getIdEntidades()) ;
	}


	public int getIdAcao() {
		return idAcao;
	}


	public void setIdAcao(int idAcao) {
		this.idAcao = idAcao;
	}


	public void setSolicitacoes(List<Solicitacao> solicitacoes) {
		this.solicitacoes = solicitacoes;
	}


	public int getIdSolicitacao() {
		return idSolicitacao;
	}


	public void setIdSolicitacao(int idSolicitacao) {
		this.idSolicitacao = idSolicitacao;
	}


	public List<Solicitacao> getFilteredSolicitacoes() {
		this.filteredSolicitacoes =  new ArrayList<Solicitacao>(SolicitacaoDAO.list());
		return filteredSolicitacoes;
	}


	public void setFilteredSolicitacoes(List<Solicitacao> filteredSolicitacoes) {
		this.filteredSolicitacoes = filteredSolicitacoes;
	}
	
	
}