package com.lyncode.oai.proxy.model.dao.impl;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lyncode.oai.proxy.model.dao.api.RepositoryDao;
import com.lyncode.oai.proxy.model.entity.User;

@Repository("RepositoryDao")
@Transactional
public class RepositoryDaoImpl implements RepositoryDao {
	private static Logger log = LogManager.getLogger(RepositoryDaoImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.openSession();
	}
	
	@Override
	public int saveRepository(
			com.lyncode.oai.proxy.model.entity.Repository repository) {
		Session session = getSession();
		Transaction tx = null;
		Integer id = null;
		try {
			tx = session.beginTransaction();
			id = (Integer) session.save(repository);
			tx.commit();
			log.debug("Repository Saved");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<com.lyncode.oai.proxy.model.entity.Repository> getAll() {
		return (List<com.lyncode.oai.proxy.model.entity.Repository>) getSession().createCriteria(com.lyncode.oai.proxy.model.entity.Repository.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<com.lyncode.oai.proxy.model.entity.Repository> selectByUserId(
			int userId) {
		return (List<com.lyncode.oai.proxy.model.entity.Repository>) getSession()
				.createCriteria(com.lyncode.oai.proxy.model.entity.Repository.class)
				.add(Restrictions.eq("userId", userId))
				.list();
	}

	@Override
	public void deleteRepository(com.lyncode.oai.proxy.model.entity.Repository r) {
		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(r);
			tx.commit();
			log.debug("Repository deleted");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

}
