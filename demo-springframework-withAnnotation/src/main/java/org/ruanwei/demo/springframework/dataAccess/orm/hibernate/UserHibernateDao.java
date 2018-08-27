package org.ruanwei.demo.springframework.dataAccess.orm.hibernate;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.ruanwei.demo.springframework.dataAccess.CrudDao;
import org.ruanwei.demo.springframework.dataAccess.orm.jpa.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hibernate Native API
 * 
 * @author ruanwei
 *
 */
@Transactional
@Repository
public class UserHibernateDao implements CrudDao<UserEntity, Integer> {
	private static Log log = LogFactory.getLog(UserHibernateDao.class);

	private static final String sql_11 = "select name from user where id = 1";
	private static final String sql_12 = "select name from user where id = ?";
	private static final String hql_12 = "from demo.User user where user.id=?";
	private static final String sql_13 = "select name from user where id = :id";

	private SessionFactory sessionFactory;
	private HibernateTemplate hibernateTemplate;

	@Required
	@Autowired
	public void setSessionFactory(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	private Session currentSession() {
		// Session session = sessionFactory.openSession();
		return sessionFactory.getCurrentSession();
	}

	@Override
	public UserEntity save(UserEntity entity) {
		currentSession().save(entity);
		return entity;
	}

	@Override
	public List<UserEntity> saveAll(List<UserEntity> entities) {
		entities.forEach(entity -> currentSession().save(entity));
		return entities;
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<UserEntity> findById(Integer id) {
		UserEntity userEntity = currentSession().get(UserEntity.class, 1);
		log.info("userEntity========" + userEntity);
		return Optional.of(userEntity);
	}

	@Transactional(readOnly = true)
	@Override
	public boolean existsById(Integer id) {
		return findById(id).isPresent();
	}

	@Transactional(readOnly = true)
	@Override
	public List<UserEntity> findAll() {
		Query<UserEntity> query = currentSession().createQuery("from UserEntity");
		List<UserEntity> result = query.list();
		result.forEach(e -> log.info("e========" + e));
		return result;
	}

	@Transactional(readOnly = true)
	@Override
	public long count() {
		return findAll().size();
	}

	@Transactional(readOnly = true)
	@Override
	public List<UserEntity> findAllById(List<Integer> ids) {
		return null;
	}

	@Override
	public void deleteById(Integer id) {

	}

	@Override
	public void delete(UserEntity entity) {
		currentSession().delete(entity);
	}

	@Override
	public void deleteAll(List<UserEntity> entities) {
		entities.forEach(entity -> currentSession().delete(entity));

	}

	@Override
	public void deleteAll() {

	}

}
