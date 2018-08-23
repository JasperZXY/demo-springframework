package org.ruanwei.demo.springframework.data.jdbc;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.ruanwei.demo.springframework.dataAccess.User;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.ColumnMapRowMapper;

/**
 * 
 * see also PagingAndSortingRepository
 * 
 * @author ruanwei
 *
 */
@RepositoryDefinition(domainClass = User.class, idClass = Integer.class)
public interface SpringDataJdbcRepository extends CrudRepository<User, Integer> {

	// ====================single row====================
	@Query("select name from user where id = :id")
	String findNameById(@Param("id") int id);

	@Query(rowMapperClass = ColumnMapRowMapper.class, value = "select name, age from user where id = :id")
	Map<String, Object> findNameAndAgeById(@Param("id") int id);

	@Query("select * from user where id = :id")
	User findUserById(@Param("id") int id);

	// ====================multiple row====================
	@Query("select name from user where id > :id")
	List<String> findNameListById(@Param("id") int id);

	@Query("select name, age from user where id > :id")
	List<Map<String, Object>> findNameAndAgeListById(@Param("id") int id);

	@Query("select * from user where id > :id")
	List<User> findUserListById(@Param("id") int id);

	// ====================update====================
	@Modifying
	@Query("insert into user(name, age, birthday) values(:name, :age, :birthday)")
	int createUser(@Param("name") String name, @Param("age") int age, @Param("birthday") Date birthday);

	@Modifying
	@Query("update user set age = :age where name = :name")
	int updateUser(@Param("name") String name, @Param("age") int age);

	@Modifying
	@Query("delete from user where id > :id")
	int deleteUser(@Param("id") int largerThanId);
}
