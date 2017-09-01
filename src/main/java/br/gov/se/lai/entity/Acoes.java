package br.gov.se.lai.Entity;
// Generated 30/08/2017 11:22:29 by Hibernate Tools 5.2.5.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Acoes generated by hbm2java
 */
@Entity
@Table(name = "acoes", catalog = "esic")
public class Acoes implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1132866265333506938L;
	private int idAcoes;
	private String titulo;
	private String descricao;
	private Set<Entidades> entidades = new HashSet<Entidades>(0);

	public Acoes() {
	}

	public Acoes(int idAcoes) {
		this.idAcoes = idAcoes;
	}

	public Acoes(int idAcoes, String titulo, String descricao, Set<Entidades> entidades) {
		this.idAcoes = idAcoes;
		this.titulo = titulo;
		this.descricao = descricao;
		this.entidades = entidades;
	}

	@Id

	@Column(name = "idAcoes", unique = true, nullable = false)
	public int getIdAcoes() {
		return this.idAcoes;
	}

	public void setIdAcoes(int idAcoes) {
		this.idAcoes = idAcoes;
	}

	@Column(name = "titulo", length = 45)
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

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "acoes_das_entidades", catalog = "esic", joinColumns = {
			@JoinColumn(name = "idAcoes", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "idEntidades", nullable = false, updatable = false) })
	public Set<Entidades> getEntidadeses() {
		return this.entidades;
	}

	public void setEntidadeses(Set<Entidades> entidades) {
		this.entidades = entidades;
	}

}