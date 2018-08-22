package org.ruanwei.demo.springframework.data.jdbc;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = User2.class, idClass = Integer.class)
public interface UserJdbcRepository {// extends Repository<User2, Integer> {

	@Query("select * from user")
	List<User2> findAll();
}
