package org.ruanwei.demo.springframework.data.jdbc;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.ruanwei.demo.springframework.dataAccess.User;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * 
 * @RepositoryDefinition 等价于 extends Repository
 * 
 * @author ruanwei
 *
 */
@Transactional(readOnly = true)
@RepositoryDefinition(domainClass = User.class, idClass = Integer.class)
public interface UserJdbcRepository extends Repository<User, Integer> {

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
	@Transactional(readOnly = false)
	@Modifying
	@Query("insert into user(name, age, birthday) values(:name, :age, :birthday)")
	int createUser(@Param("name") String name, @Param("age") int age, @Param("birthday") Date birthday);

	@Transactional(readOnly = false)
	@Modifying
	@Query("update user set age = :age where name = :name")
	int updateUser(@Param("name") String name, @Param("age") int age);

	@Transactional(readOnly = false)
	@Modifying
	@Query("delete from user where id > :id")
	int deleteUser(@Param("id") int largerThanId);

	// ====================async query====================
	@Async
	@Query("select * from user")
	Future<List<User>> findAllUser1();

	@Async
	@Query("select * from user")
	CompletableFuture<List<User>> findAllUser2();

	@Async
	@Query("select * from user")
	ListenableFuture<List<User>> findAllUser3();
}
