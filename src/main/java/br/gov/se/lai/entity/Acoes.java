package br.gov.se.lai.entity;
// Generated 18/09/2017 08:12:36 by Hibernate Tools 5.2.5.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Acoes generated by hbm2java
 */
@Entity
@Table(name = "acoes", catalog = "esic")
public class Acoes implements java.io.Serializable {

	private static final long serialVersionUID = 8345153301601548063L;
	private Integer idAcoes;
	private String titulo;
	private String descricao;
	private String status;
	private Set<Competencias> competenciases = new HashSet<Competencias>(0);

	public Acoes() {
	}

	public Acoes(String titulo, String descricao, Set<Competencias> competenciases, String status) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.competenciases = competenciases;
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "idAcoes", unique = true, nullable = false)
	public Integer getIdAcoes() {
		return this.idAcoes;
	}

	public void setIdAcoes(Integer idAcoes) {
		this.idAcoes = idAcoes;
	}

	@Column(name = "titulo", length = 60)
	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	@Column(name = "descricao", length = 65535)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "acoes")
	public Set<Competencias> getCompetenciases() {
		return this.competenciases;
	}

	public void setCompetenciases(Set<Competencias> competenciases) {
		this.competenciases = competenciases;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}
