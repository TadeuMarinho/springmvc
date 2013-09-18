package com.springmvc.exemplo.servico;

import com.springmvc.exemplo.entidade.Usuario;
import com.springmvc.exemplo.infra.negocio.IService;

public interface IUsuarioService extends IService<Usuario, Integer> {
	
	void logar(Usuario usuario);
	
	Usuario buscarPorLogin(String login);
}
