package org.ruanwei.demo.springframework.dataAccess.orm.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.dataAccess.orm.jpa.entity.UserEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class UserJpaDao {
	private static Log log = LogFactory.getLog(UserJpaDao.class);

	private static final String sql_11 = "select name from user where id = 1";
	private static final String sql_12 = "select name from user where id = ?";
	private static final String jpql_12 = "from User as u where u.age = ?1";
	private static final String sql_13 = "select name from user where id = :id";

	@PersistenceContext
	private EntityManager entityManager;

	private EntityManagerFactory entityManagerFactory;

	@PersistenceUnit
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Transactional(readOnly = true)
	public void queryForSingleRowWithSingleColumn(int id) {
		try {
			Query query = entityManager.createQuery(jpql_12);
			query.setParameter(1, 1);
			List<UserEntity> list = query.getResultList();
			list.forEach(e -> log.info("e========" + e));
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}
}
