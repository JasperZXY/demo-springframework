package org.ruanwei.demo.springframework.data.jdbc;

import org.ruanwei.demo.springframework.dataAccess.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * 
 * see also SimpleKeyValueRepository
 * 
 * @author ruanwei
 *
 */
@RepositoryDefinition(domainClass = User.class, idClass = Integer.class)
public interface JdbcPagingAndSortingRepository extends PagingAndSortingRepository<User, Integer> {

}
