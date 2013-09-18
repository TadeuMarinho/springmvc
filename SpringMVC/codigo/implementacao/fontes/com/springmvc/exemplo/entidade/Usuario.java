package com.springmvc.exemplo.entidade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.springmvc.exemplo.infra.persistencia.ObjetoPersistente;

@Entity
@Table(name="usuario")
@SequenceGenerator(name=Usuario.NOME_SEQ, sequenceName=Usuario.NOME_SEQ)
@SuppressWarnings("serial")
public class Usuario extends ObjetoPersistente implements UserDetails {
	
	protected static final String NOME_SEQ = "usuario_id_seq";
	
	private String login;
	private String senha;
	private List<Perfil> perfis;
	
	public Usuario() {
		this.perfis = new ArrayList<>();
	}
	
	@Id
	@Column(name="id", nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO, generator=Usuario.NOME_SEQ)
	@Override
	public Integer getId() {
		return super.id;
	}
	
	@NotNull
	@NotBlank
	@Size(min=5, max=10, message="Login muito curto ou muito longo")
	@Column(name="login", nullable=false)
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}

	@NotNull
	@NotBlank
	@Size(min=5, max=10, message="Senha muito curta ou muito longa")
	@Column(name="senha", nullable=false)
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}

	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="usuario_perfil", 
		joinColumns = {@JoinColumn(name="id_usuario")}, 
		inverseJoinColumns = {@JoinColumn(name="id_perfil")})
	public List<Perfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}

	@Override
	public String toString() {
		return getLogin();
	}
	
	@Override
	public boolean equals(Object objeto) {
		boolean saoIguais = false;
		
		if (objeto == this) {
			saoIguais = true;
		} else if (objeto instanceof Usuario) {
			Usuario usuario = (Usuario) objeto;
			saoIguais = new EqualsBuilder().append(login, usuario.getLogin()).isEquals();
		}
		
		return saoIguais;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(login).hashCode();
	}

	//---- Específicos para o Spring Security.
	
	@Override
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (Perfil perfil : this.perfis) {
			authorities.add(new SimpleGrantedAuthority(perfil.getNome()));
		}
		return authorities;
	}

	@Override
	@Transient
	public String getPassword() {
		return getSenha();
	}

	@Override
	@Transient
	public String getUsername() {
		return getLogin();
	}

	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@Transient
	public boolean isEnabled() {
		return true;
	}
}