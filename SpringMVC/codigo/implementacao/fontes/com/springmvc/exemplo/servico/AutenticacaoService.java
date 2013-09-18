package com.springmvc.exemplo.servico;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springmvc.exemplo.ConstantesMensagens;
import com.springmvc.exemplo.entidade.Usuario;
import com.springmvc.exemplo.infra.excecao.NegocioException;

/**
 * Serviço utilizado para autenticação e autorização.
 */
@Service
public class AutenticacaoService implements IAutenticacaoService, UserDetailsService {
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	/** {@inheritDoc} */
	public void autenticar(String login, String senha) {
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, senha);
			Authentication autenticacao = this.authenticationManager.authenticate(token);
			
			if (autenticacao.isAuthenticated()) {
				SecurityContextHolder.getContext().setAuthentication(autenticacao);
			}
		} catch (AuthenticationException e) {
			throw new NegocioException(ConstantesMensagens.ERRO_LOGIN);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public Usuario recuperarUsuarioLogado() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean possuiPerfil() {
		return !SecurityContextHolder.getContext().getAuthentication().getAuthorities().isEmpty();
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean possuiPerfil(String nomePerfil) {
		boolean possuiPerfil = false;
		
		for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			if (nomePerfil.equals(authority.getAuthority())) {
				possuiPerfil = true;
				break;
			}
		}
		
		return possuiPerfil;
	}
	
	/** {@inheritDoc} */
	@Override
	public void logout(HttpSession sessao) {
		SecurityContextHolder.getContext().setAuthentication(null);
		sessao.invalidate();
	}
	
	/** {@inheritDoc} */
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Usuario usuario = usuarioService.buscarPorLogin(login);
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário com o login " + login + " não encontado."); //TODO trmf Trocar por uma NegocioException
		}
		
		return usuario;
	}
}