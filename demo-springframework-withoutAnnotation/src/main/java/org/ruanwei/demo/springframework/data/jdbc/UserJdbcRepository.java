package org.ruanwei.demo.springframework.data.jdbc;

import org.ruanwei.demo.springframework.dataAccess.User;
import org.springframework.data.repository.CrudRepository;

public interface UserJdbcRepository extends CrudRepository<User, Integer> {

}
