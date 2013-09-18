package com.springmvc.exemplo.dao;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.springmvc.exemplo.entidade.Usuario;
import com.springmvc.exemplo.infra.persistencia.hibernate.AbstractHibernateDAO;

@Repository
@SuppressWarnings("serial")
public class UsuarioDAO extends AbstractHibernateDAO<Usuario, Integer> {

	@Override
	protected Class<Usuario> getClassePersistente() {
		return Usuario.class;
	}
	
	public Usuario buscarPorLogin(String login) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("login", login));
		criteria.setFetchMode("perfis", FetchMode.JOIN);
		return (Usuario) criteria.uniqueResult();
 	}
}