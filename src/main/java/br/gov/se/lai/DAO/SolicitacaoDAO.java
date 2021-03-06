package br.gov.se.lai.DAO;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.se.lai.Bean.UsuarioBean;
import br.gov.se.lai.entity.Entidades;
import br.gov.se.lai.entity.Responsavel;
import br.gov.se.lai.entity.Solicitacao;
import br.gov.se.lai.utils.Consultas;
import br.gov.se.lai.utils.HibernateUtil;

public class SolicitacaoDAO {
	
	private static EntityManager em = HibernateUtil.geteEntityManagerFactory().createEntityManager();
	
    public static boolean saveOrUpdate(Solicitacao solicitacao) {     	        
        try {
        	if(!em.getTransaction().isActive()) em.getTransaction().begin();
        	if(solicitacao.getIdSolicitacao() ==  null) {
        		em.persist(solicitacao);
    		}else {
    			em.merge(solicitacao);
    		}
            em.getTransaction().commit();
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Solicitacao salvo(a) com sucesso!"));
            return true;
        } catch (Exception e) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Erro ao cadastrar solicitacao "));
        	System.out.println(e);
            em.getTransaction().rollback();
            return false;
        }
    }
    public static void delete(Solicitacao solicitacao) {        
        try {            
        	Solicitacao soli = em.find(Solicitacao.class, solicitacao.getIdSolicitacao());
            if (soli == null) {
            	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Solicitacao  n�o cadastrado."));
			}else {
				em.getTransaction().begin();
				em.remove(soli);
	            em.getTransaction().commit();
//	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Solicitacao  excluída com sucesso!"));
			}            
        } catch (Exception e) {
            em.getTransaction().rollback();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Erro ao excluir solicitacao "));
        }
    }    
    
    public static Solicitacao findSolicitacao(int idSolicitacao){
    	return em.find(Solicitacao.class, idSolicitacao) ;
    }
    

	@SuppressWarnings("unchecked")
	public static List<Solicitacao> list() {
		UsuarioBean usuarioBean = (UsuarioBean) HibernateUtil.RecuperarDaSessao("usuario");
		if(usuarioBean != null) 
		{
			if(usuarioBean.getUsuario().getPerfil() == 3 || usuarioBean.getUsuario().getPerfil() == 4 && !usuarioBean.isPerfilAlterarCidadaoResponsavel()) 
			{
				return (List<Solicitacao>) Consultas.buscaPersonalizada("FROM Solicitacao as slt WHERE slt.cidadao.usuario.idUsuario = "+usuarioBean.getUsuario().getIdUsuario(),em);
			}else 
			{
				if(usuarioBean.getUsuario().getPerfil() == 2 || usuarioBean.getUsuario().getPerfil() == 4 && usuarioBean.isPerfilAlterarCidadaoResponsavel()) 
				{
					List<Responsavel> ListResp = new ArrayList<Responsavel>(ResponsavelDAO.findResponsavelUsuario(usuarioBean.getUsuario().getIdUsuario()));
					
					String query = "FROM Solicitacao as slt WHERE   ";
							
					for (int i = 0; i < ListResp.size(); i++) {
						if (ListResp.get(i).isAtivo()) {
							if(query.contains("slt.entidades.idEntidades")) {
								query += " OR ";
							}
							query += " slt.entidades.idEntidades = ";
							query = query +ListResp.get(i).getEntidades().getIdEntidades()+" AND (slt.instancia = 1";
							for (int j = 2 ;  j <= usuarioBean.getResponsavel().getNivel(); j++) {
								query = query+" OR slt.instancia = "+ j;
							}
							query += ")";
						}
//						if(i == ListResp.size()-1) {
//							query += ")";
//						}else {
//							query += ") OR ";
//						}

					}
					
					return (List<Solicitacao>) Consultas.buscaPersonalizada(query,em);
				}else 
				{
					return  em.createNativeQuery("SELECT * FROM esic.solicitacao", Solicitacao.class).getResultList();		
				}
				
			} 
		}else
		{	
			return null;
		}  
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<Solicitacao> listPorStatus(String status) {
			return (List<Solicitacao>) (Consultas.buscaPersonalizada("FROM Solicitacao as slt WHERE slt.status = "+status,em)); 
	}	
		
	@SuppressWarnings("unchecked")
	public static List<Solicitacao> listarPorEntidade(int idEntidade) {
		return (List<Solicitacao>) (Consultas.buscaPersonalizada("FROM Solicitacao as slt WHERE slt.entidades.idEntidades = "+idEntidade,em)); 
	}	
	
	@SuppressWarnings("unchecked")
	public static List<Solicitacao> listarGeral() {
		String status = "Finalizada";
		Query query = em.createQuery("FROM Solicitacao as slt WHERE slt.status !=  :sltParam");
		query.setParameter("sltParam", status);
    	
    	List<Solicitacao> results = query.getResultList();
    	return results;    	
    } 
		

}
