package org.ruanwei.demo.springframework.dataAccess.orm.jpa;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.dataAccess.CrudDao;
import org.ruanwei.demo.springframework.dataAccess.orm.jpa.entity.UserEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * Java Persistence API based on Hibernate
 * 
 * @author ruanwei
 *
 */
@Transactional
//@Repository
public class UserJpaDao implements CrudDao<UserEntity, Integer> {
	private static Log log = LogFactory.getLog(UserJpaDao.class);

	private static final String sql_11 = "select name from user where id = 1";
	private static final String sql_12 = "select name from user where id = ?";
	private static final String jpql_12 = "from User as u where u.age = ?1";
	private static final String sql_13 = "select name from user where id = :id";

	@PersistenceContext
	private EntityManager entityManager;

	private EntityManagerFactory entityManagerFactory;

	@PersistenceUnit(name = "entityManagerFactory")
	public void setEntityManagerFactory(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public <S extends UserEntity> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends UserEntity> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<UserEntity> findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public boolean existsById(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Transactional(readOnly = true)
	@Override
	public List<UserEntity> findAll() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			Query query = entityManager.createQuery(jpql_12);
			query.setParameter(1, 1);
			List<UserEntity> list = query.getResultList();
			list.forEach(e -> log.info("e========" + e));
			return list;
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}

	}

	@Transactional(readOnly = true)
	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Transactional(readOnly = true)
	@Override
	public Iterable<UserEntity> findAllById(Iterable<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(UserEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(Iterable<? extends UserEntity> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

}
