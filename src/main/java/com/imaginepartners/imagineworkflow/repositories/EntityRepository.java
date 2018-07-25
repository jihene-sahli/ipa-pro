package com.imaginepartners.imagineworkflow.repositories;

import java.io.Serializable;
import java.util.List;

public interface EntityRepository<T, ID extends Serializable> {
	/**
	 *
	 * @param primaryKey
	 * @return
	 */
	T findOne(ID primaryKey);

	/**
	 *
	 * @param className
	 * @param attribut
	 * @param value
	 * @return
	 */
	T findOneByAttribut(Class className, String attribut, String value);

	/**
	 *
	 * @param className
	 * @return
	 */
	List<T> findAll(Class className);

	/**
	 *
	 * @param className
	 * @return
	 */
	Long count(Class className);

	/**
	 *
	 * @param className
	 * @param attribut
	 * @param value
	 * @return
	 */
	List<T> findAllByAttribut(Class className, String attribut, Object value);

	/**
	 *
	 * @param className
	 * @param value
	 * @return
	 */
	Object add(Class className, Object value);

	/**
	 *
	 * @param className
	 * @param value
	 * @return
	 */
	Object update(Class className, Object value);
}
