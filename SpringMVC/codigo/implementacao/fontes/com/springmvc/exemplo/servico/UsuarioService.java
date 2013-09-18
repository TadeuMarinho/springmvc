package com.springmvc.exemplo.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.springmvc.exemplo.ConstantesMensagens;
import com.springmvc.exemplo.dao.UsuarioDAO;
import com.springmvc.exemplo.entidade.Usuario;
import com.springmvc.exemplo.infra.excecao.NegocioException;
import com.springmvc.exemplo.infra.negocio.AbstractService;
import com.springmvc.exemplo.infra.persistencia.hibernate.AbstractHibernateDAO;

@SuppressWarnings("serial")
@Service
public class UsuarioService extends AbstractService<Usuario, Integer> implements IUsuarioService {
	
	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Transactional(isolation = Isolation.DEFAULT, readOnly = true)
	public void logar(Usuario usuario) {
		if (this.usuarioDAO.buscarPorModelo(usuario) == null) {
			throw new NegocioException(ConstantesMensagens.ERRO_LOGIN);
		}
	}
	
	@Transactional(isolation = Isolation.DEFAULT, readOnly = true)
	public Usuario buscarPorLogin(String login) {
		return this.usuarioDAO.buscarPorLogin(login);
	}
	

	@Override
	protected AbstractHibernateDAO<Usuario, Integer> getDao() {
		return this.usuarioDAO;
	}
}