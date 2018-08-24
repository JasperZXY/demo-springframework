package org.ruanwei.demo.springframework.data.jdbc;

import org.ruanwei.demo.springframework.dataAccess.User;
import org.springframework.data.repository.CrudRepository;

/**
 * 
 * see also SimpleJdbcRepository
 * 
 * @author ruanwei
 *
 */
public interface UserJdbcCrudRepository extends CrudRepository<User, Integer> {

}
