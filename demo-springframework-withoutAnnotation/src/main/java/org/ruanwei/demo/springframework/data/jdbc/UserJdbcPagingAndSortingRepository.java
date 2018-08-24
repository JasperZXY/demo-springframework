package org.ruanwei.demo.springframework.data.jdbc;

import org.ruanwei.demo.springframework.dataAccess.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserJdbcPagingAndSortingRepository extends PagingAndSortingRepository<User, Integer> {

}
