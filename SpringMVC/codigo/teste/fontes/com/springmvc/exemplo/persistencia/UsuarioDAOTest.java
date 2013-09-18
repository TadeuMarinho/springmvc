package com.springmvc.exemplo.persistencia;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.springmvc.exemplo.dao.UsuarioDAO;
import com.springmvc.exemplo.entidade.Usuario;
import com.springmvc.exemplo.entidade.UsuarioUtil;

@FixMethodOrder(MethodSorters.JVM)
public class UsuarioDAOTest extends AbstractDAOTest {
	
	private static Usuario usuario;
	
	@Autowired
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	@BeforeClass
	public static void configurar() {
		usuario = UsuarioUtil.criar();
	}

	@Override
	@Test
	public void salvar() {
		Assert.assertNotNull(this.usuarioDAO.salvar(usuario));
		Assert.assertEquals(usuario, this.usuarioDAO.buscar(usuario.getId()));
	}

	@Override
	@Test
	public void alterar() {
		Assert.assertEquals(usuario, this.usuarioDAO.buscarDesconectado(usuario.getId()));
		usuario.setLogin("loginAlt");
		this.usuarioDAO.alterar(usuario);
		Assert.assertEquals(usuario.getLogin(), this.usuarioDAO.buscar(usuario.getId()).getLogin());
	}

	@Override
	@Test
	public void excluir() {
		this.usuarioDAO.excluir(usuario);
		Assert.assertEquals(null, this.usuarioDAO.buscar(usuario.getId()));
	}
}
