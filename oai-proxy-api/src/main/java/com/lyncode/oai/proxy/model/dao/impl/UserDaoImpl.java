package com.lyncode.oai.proxy.model.dao.impl;

import java.math.BigInteger;
import java.security.SecureRandom;
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

import com.lyncode.oai.proxy.model.dao.api.UserDao;
import com.lyncode.oai.proxy.model.entity.User;
import com.lyncode.oai.proxy.util.CryptoUtils;

@Repository("UserDao")
@Transactional
public class UserDaoImpl implements UserDao {
	private static Logger log = LogManager.getLogger(UserDaoImpl.class);
	private static SecureRandom random = new SecureRandom();

	@Autowired
	SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.openSession();
	}

	public int saveUser(User user) {
		Session session = getSession();
		Transaction tx = null;
		Integer employeeID = null;
		try {
			tx = session.beginTransaction();
			user.setPassword(CryptoUtils.sha1(user.getPassword()));
			employeeID = (Integer) session.save(user);
			tx.commit();
			log.debug("User Saved");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return employeeID;
	}

	@Transactional(readOnly = false)
	public void deleteUser(User user) {
		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(user);
			tx.commit();
			log.debug("User deleted");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<User> getAllUser(User user) {
		return (List<User>) getSession().createCriteria(User.class).list();
	}

	public User selectUserById(int userId) {
		return (User) getSession().get(User.class, userId);
	}

	@Override
	public User selectUserByEmail(String email) {
		return (User) getSession().createCriteria(User.class)
				.add(Restrictions.eq("email", email)).uniqueResult();
	}

	@Override
	public String generateActivationKey() {
		String key = new BigInteger(130, random).toString(32);
		Object obj = selectUserByActivationKey(key);
		if (obj != null) {
			return generateActivationKey();
		} else {
			return key;
		}
	}

	@Override
	public User selectUserByActivationKey(String key) {
		return (User) getSession().createCriteria(User.class)
				.add(Restrictions.eq("activationKey", key)).uniqueResult();
	}

}
