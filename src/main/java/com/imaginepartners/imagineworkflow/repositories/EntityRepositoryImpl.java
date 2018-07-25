package com.imaginepartners.imagineworkflow.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Repository
@Transactional("transactionManager")
public class EntityRepositoryImpl implements EntityRepository, Serializable {

	private static final long serialVersionUID = 1L;

	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	public EntityRepositoryImpl() {

	}

	public Session getCurrentSession() {
		if (sessionFactory.getCurrentSession() == null) {
			sessionFactory.openSession();
		}
		return sessionFactory.getCurrentSession();
	}

	@Override
	public Object findOne(Serializable primaryKey) {
		return null;
	}

	@Override
	public Object findOneByAttribut(Class className, String attribut, String value) {
		Object entity = getCurrentSession().createCriteria(className)
		.add(Restrictions.eq(attribut, value))
		.uniqueResult();
		return entity;
	}

	@Override
	public List findAll(Class className) {
		List entities = getCurrentSession().createCriteria(className).list();
		return entities;
	}

	@Override
	public Long count(Class className) {
		Long count = (Long) getCurrentSession().createCriteria(className)
		.setProjection(Projections.rowCount())
		.uniqueResult();
		return count;
	}

	@Override
	public List findAllByAttribut(Class className, String attribut, Object value) {
		List entities = getCurrentSession().createCriteria(className)
		.add(Restrictions.eq(attribut, value))
		.list();
		return entities;
	}

	@Override
	public Object add(Class className, Object body) {
		getCurrentSession().saveOrUpdate(className.getName(), body);
		return body;
	}

	@Override
	public Object update(Class className, Object body) {
		getCurrentSession().saveOrUpdate(className.getName(), body);
		return body;
	}
}
