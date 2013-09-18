package com.springmvc.exemplo.servicoweb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springmvc.exemplo.entidade.Usuario;
import com.springmvc.exemplo.servico.IUsuarioService;

@Controller
@RequestMapping("/webservice/usuario")
public class UsuarioWSController {

	@Autowired
	private IUsuarioService usuarioService;
	
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	@ResponseBody
	public List<Usuario> listarUsuarios() {
		return this.usuarioService.listar("login");
	}
}