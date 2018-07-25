package com.imaginepartners.imagineworkflow.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.annotation.Resource;

public class Repository  {

	private static final long serialVersionUID = 1L;

	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	public Session getCurrentSession() {
		if (sessionFactory.getCurrentSession() == null) {
			sessionFactory.openSession();
		}
		return sessionFactory.getCurrentSession();
	}
}
