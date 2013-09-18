package com.springmvc.exemplo.servicoweb.dto;

import java.util.List;

import com.springmvc.exemplo.entidade.Perfil;

public class UsuarioDTO {
	
	private String login;
	private List<Perfil> perfis;
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public List<Perfil> getPerfis() {
		return perfis;
	}
	
	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}
}