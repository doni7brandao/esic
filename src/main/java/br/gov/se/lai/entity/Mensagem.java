package br.gov.se.lai.entity;
// Generated 04/09/2017 09:25:00 by Hibernate Tools 5.2.5.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Mensagem generated by hbm2java
 */
@Entity
@Table(name = "mensagem", catalog = "esic")
public class Mensagem implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -117379702782850057L;
	private Integer idMensagem;
	private Solicitacao solicitacao;
	private Usuario usuario;
	private String texto;
	private String anexo;
	private Date data;

	public Mensagem() {
	}

	public Mensagem(Solicitacao solicitacao, Usuario usuario) {
		this.solicitacao = solicitacao;
		this.usuario = usuario;
	}

	public Mensagem(Solicitacao solicitacao, Usuario usuario, String texto, String anexo, Date data) {
		this.solicitacao = solicitacao;
		this.usuario = usuario;
		this.texto = texto;
		this.anexo = anexo;
		this.data = data;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "idMensagem", unique = true, nullable = false)
	public Integer getIdMensagem() {
		return this.idMensagem;
	}

	public void setIdMensagem(Integer idMensagem) {
		this.idMensagem = idMensagem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idSolicitacao", nullable = false)
	public Solicitacao getSolicitacao() {
		return this.solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idUsuario", nullable = false)
	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Column(name = "texto", length = 65535)
	public String getTexto() {
		return this.texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	@Column(name = "anexo", length = 65535)
	public String getAnexo() {
		return this.anexo;
	}

	public void setAnexo(String anexo) {
		this.anexo = anexo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", length = 19)
	public Date getData() {
		return this.data;
	}

	public void setData(Date data) {
		this.data = data;
	}

}
