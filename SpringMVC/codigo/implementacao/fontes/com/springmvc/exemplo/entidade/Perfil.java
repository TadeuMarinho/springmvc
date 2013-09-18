package com.springmvc.exemplo.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;

import com.springmvc.exemplo.infra.persistencia.ObjetoPersistente;

@Entity
@Table(name="perfil")
@SequenceGenerator(name=Perfil.NOME_SEQ, sequenceName=Perfil.NOME_SEQ)
@SuppressWarnings("serial")
public class Perfil extends ObjetoPersistente {
	
	protected static final String NOME_SEQ = "perfil_id_seq";
	
	private String nome;

	@Id
	@Column(name="id", nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO, generator=Perfil.NOME_SEQ)
	@Override
	public Integer getId() {
		return super.id;
	}

	@NotNull
	@NotBlank
	@Size(max=20, message="Nome do perfil muito grande")
	@Column(name="nome", nullable=false)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		return getNome();
	}
	
	@Override
	public boolean equals(Object objeto) {
		boolean saoIguais = false;
		
		if (objeto == this) {
			saoIguais = true;
		} else if (objeto instanceof Perfil) {
			Perfil perfil = (Perfil) objeto;
			saoIguais = new EqualsBuilder().append(nome, perfil.getNome()).isEquals();
		}
		
		return saoIguais;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(nome).hashCode();
	}
}