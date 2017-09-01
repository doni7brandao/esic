package br.gov.se.lai.Entity;
// Generated 30/08/2017 11:22:29 by Hibernate Tools 5.2.5.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Mensagem generated by hbm2java
 */
@Entity
@Table(name = "mensagem", catalog = "esic")
public class Mensagem implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3496744523778523796L;
	private int idMensagem;
	private Solicitacao solicitacao;
	private String texto;
	private String anexo;

	public Mensagem() {
	}

	public Mensagem(int idMensagem, Solicitacao solicitacao) {
		this.idMensagem = idMensagem;
		this.solicitacao = solicitacao;
	}

	public Mensagem(int idMensagem, Solicitacao solicitacao, String texto, String anexo) {
		this.idMensagem = idMensagem;
		this.solicitacao = solicitacao;
		this.texto = texto;
		this.anexo = anexo;
	}

	@Id

	@Column(name = "idMensagem", unique = true, nullable = false)
	public int getIdMensagem() {
		return this.idMensagem;
	}

	public void setIdMensagem(int idMensagem) {
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

}
