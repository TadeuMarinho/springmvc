package com.springmvc.exemplo.entidade;

public class PerfilUtil {

	public static Perfil criar() {
		return criar("Admin");
	}

	public static Perfil criar(String nome) {
		Perfil perfil = new Perfil();
		perfil.setNome(nome);
		
		return perfil;
	}
}
