package org.ruanwei.demo.springframework.data.jdbc;

import org.ruanwei.demo.springframework.dataAccess.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * 
 * see also SimpleJdbcRepository
 * 
 * @author ruanwei
 *
 */
@RepositoryDefinition(domainClass = User.class, idClass = Integer.class)
public interface JdbcCrudRepository extends CrudRepository<User, Integer> {

}
