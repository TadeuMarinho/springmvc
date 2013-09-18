package com.springmvc.exemplo.infra.persistencia;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

public class HibernateSpringUtils {

	private static final Logger LOGGER = Logger.getLogger(HibernateSpringUtils.class);
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void setSessionFactory(SessionFactory sessionFactory) {
		HibernateSpringUtils.sessionFactory = sessionFactory;
	}

	public static void destroySession() {
		if (sessionFactory == null) {
			return;
		}

		if (!sessionFactory.isClosed()) {
			sessionFactory.close();
		}

		setSessionFactory(null);
		LOGGER.info("Sessão fechada!");
	}
}