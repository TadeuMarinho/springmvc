package com.springmvc.exemplo.controlador;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springmvc.exemplo.ConstantesMensagens;
import com.springmvc.exemplo.entidade.Usuario;
import com.springmvc.exemplo.infra.excecao.NegocioException;
import com.springmvc.exemplo.servico.IAutenticacaoService;
import com.springmvc.exemplo.servico.IUsuarioService;

@Controller
public class LoginViewController extends AbstractViewController {
	
	private static final String ERRO_ACESSO_NEGADO = "acessonegado";
	private static final String ERRO_TIMEOUT = "timeout";

	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IAutenticacaoService autenticacaoService;
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String visualizarLogin(Model modelo, @RequestParam(required=false) String erro) {
		verificarErros(modelo, erro);
		if (modelo.asMap().get("usuario") == null) {
			modelo.asMap().put("usuario", new Usuario());
		}
		
		return "login";
	}

	private void verificarErros(Model modelo, String erro) {
		if (ERRO_ACESSO_NEGADO.equals(erro)) {
			adicionarMensagemErro(modelo, new NegocioException(ConstantesMensagens.ERRO_LOGIN_ACESSO_NEGADO));
		} else if (ERRO_TIMEOUT.equals(erro)) {
			adicionarMensagemErro(modelo, new NegocioException(ConstantesMensagens.ERRO_LOGIN_TIMEOUT));
		}
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String autenticar(@Valid Usuario usuario, BindingResult bindingResult, Model modelo, HttpSession sessao) {
		if (bindingResult.hasErrors()) {
	        return "login";
		}
		
		try {
			this.autenticacaoService.autenticar(usuario.getLogin(), usuario.getSenha());
			sessao.setAttribute("usuarioLogado", usuario);
		} catch (NegocioException e) {
			super.adicionarMensagemErro(modelo, e);
		}
		
		return "login";
	}
	
	@RequestMapping(value="/sair", method=RequestMethod.GET)
	public String sair(HttpSession sessao) {
		this.autenticacaoService.logout(sessao);
		return "redirect:/login.do";
	}
	
	@RequestMapping(value="/acessonegado")
	public String acessoNegado(Model modelo) {
		return "redirect:/login.do?erro=" + ERRO_ACESSO_NEGADO;
	}
	
	@RequestMapping(value="/timeout") 
	public String timeout(Model modelo) {
		return "redirect:/login.do?erro=" + ERRO_TIMEOUT;
	}
}