package com.springmvc.exemplo.servico;

import javax.servlet.http.HttpSession;

import com.springmvc.exemplo.entidade.Usuario;


public interface IAutenticacaoService {

	/**
	 * Autentica o usu·rio no sistema.
	 * 
	 * @param login Login do usu·rio.
	 * @param senha Senha di usu·rio.
	 */
	void autenticar(String login, String senha);
	
	/**
	 * Recupera o usu·rio logado no sistema.
	 * 
	 * @return usu·rio logado no sistema.
	 */
	Usuario recuperarUsuarioLogado();
	
	/**
	 * Verifica se o usu·rio logado possui algum perfil.
	 * 
	 * @return true caso o usu·rio logado possua algum perfil e false caso contr·rio.
	 */
	boolean possuiPerfil();
	
	/**
	 * Verifica se o usu·rio logado possui o perfil com o nome informado.
	 * 
	 * @param nomePerfil Nome do perfil a ser verificado.
	 * @return true caso o usu·rio logado possua o perfil e false caso contr·rio.
	 */
	boolean possuiPerfil(String nomePerfil);
	
	/**
	 * Realiza o logout do sistema.
	 * 
	 * @param sessao Sess√£o do usu·rio.
	 */
	void logout(HttpSession sessao);
}