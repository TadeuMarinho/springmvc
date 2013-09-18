package com.springmvc.exemplo.entidade;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária para criação de usuários para testes. 
 */
public class UsuarioUtil {

	public static Usuario criar() {
		return criar("login", "senha");
	}
	
	public static Usuario criar(String login, String senha) {
		List<Perfil> perfis = new ArrayList<>();
		perfis.add(PerfilUtil.criar("Perfil1"));
		perfis.add(PerfilUtil.criar("Perfil2"));
		
		Usuario usuario = new Usuario();
		usuario.setLogin(login);
		usuario.setSenha(senha);
		usuario.setPerfis(perfis);
		
		return usuario;
	}
}