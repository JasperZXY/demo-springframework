package org.ruanwei.demo.springframework.dataAccess.springdata.jdbc;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = User2.class, idClass = Integer.class)
public interface JdbcRepository extends CrudRepository<User2, Integer> {

}
