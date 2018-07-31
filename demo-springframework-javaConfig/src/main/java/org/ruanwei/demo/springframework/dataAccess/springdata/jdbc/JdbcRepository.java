package org.ruanwei.demo.springframework.dataAccess.springdata.jdbc;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

@RepositoryDefinition(domainClass = User2.class, idClass = Integer.class)
public interface JdbcRepository extends CrudRepository<User2, Integer> {

}
