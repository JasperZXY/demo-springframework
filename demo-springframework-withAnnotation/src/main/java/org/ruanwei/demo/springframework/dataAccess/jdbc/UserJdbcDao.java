package org.ruanwei.demo.springframework.dataAccess.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.dataAccess.CrudDao;
import org.ruanwei.demo.springframework.dataAccess.ExampleDao;
import org.ruanwei.demo.springframework.dataAccess.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.jdbc.object.UpdatableSqlQuery;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * JdbcDaoSupport提供了setDataSource支持 NamedParameterJdbcTemplate支持IN表达式
 * 
 * @author ruanwei
 *
 */
@Transactional
@Repository
public class UserJdbcDao implements CrudDao<User, Integer>, ExampleDao<User> {
	private static Log log = LogFactory.getLog(UserJdbcDao.class);

	// 1.core JdbcTemplate & NamedParameterJdbcTemplate thread-safe
	private JdbcTemplate jdbcTemplate;
	// named parameters instead of the traditional JDBC "?" placeholders.
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private AdvancedJdbcTemplate advancedJdbcTemplate;

	// 2.core SimpleJdbc classes
	private SimpleJdbcInsert simpleJdbcInsert;
	private SimpleJdbcCall simpleJdbcCall;// 执行存储过程或者函数

	// 3.RdbmsOperation objects.
	private SqlQuery<User> sqlQuery;
	private MappingSqlQuery<User> mappingSqlQuery;
	private UpdatableSqlQuery<User> updatableSqlQuery;
	private SqlUpdate sqlUpdate;
	private StoredProcedure storedProcedure;

	private static final String insert_sql_11 = "insert into user(name,age,birthday) values(?, ?, ?)";
	private static final String insert_sql_12 = "insert into user(name,age,birthday) values(:name, :age, :birthday)";

	private static final String select_sql_11 = "select * from user where id = ?";
	private static final String select_sql_12 = "select * from user where id = :id";
	private static final String select_sql_2 = "select * from user";
	private static final String select_sql_3 = "select count(*) from user";

	private static final String delete_sql_11 = "delete from user where id = ?";
	private static final String delete_sql_12 = "delete from user where id = :id";
	private static final String delete_sql_2 = "delete from user";
	private static final String delete_sql_31 = "delete from user where name = ? and age = ? and birthday = ?";
	private static final String delete_sql_32 = "delete from user where name = :name and age = :age and birthday = :birthday";

	private static final PreparedStatementSetter pss0 = ps -> ps.setLong(1, 0L);

	@Required
	@Autowired
	public void setDataSource(@Qualifier("jdbcDataSource") DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		this.advancedJdbcTemplate = new AdvancedJdbcTemplate(dataSource);

		this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("user").usingGeneratedKeyColumns("id");
		this.simpleJdbcCall = new SimpleJdbcCall(dataSource);
	}

	// ====================CrudDao====================
	@Override
	public User save(User entity) {
		createUser1(entity);
		createUser2(entity);
		createUser3(entity);
		createUser4(entity);
		return entity;
	}

	@Override
	public List<User> saveAll(List<User> entities) {
		batchCreateUser1(entities);
		batchCreateUser2(entities);
		batchCreateUser3(entities);
		return entities;
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<User> findById(Integer id) {
		return findByExample(select_sql_11, new Object[] { id });
	}

	@Transactional(readOnly = true)
	public Optional<User> findById2(Integer id) {

		Map<String, Integer> namedParam = new HashMap<String, Integer>();
		namedParam.put("id", id);
		return findByExample(select_sql_12, namedParam);
	}

	@Transactional(readOnly = true)
	@Override
	public boolean existsById(Integer id) {
		return findById(id).isPresent();
	}

	@Transactional(readOnly = true)
	public boolean existsById2(Integer id) {
		return findById2(id).isPresent();
	}

	@Transactional(readOnly = true)
	@Override
	public List<User> findAll() {
		return findAllByExample(select_sql_2);
	}

	@Transactional(readOnly = true)
	@Override
	public long count() {
		return jdbcTemplate.queryForObject(select_sql_3, Long.class);
	}

	@Transactional(readOnly = true)
	@Override
	public List<User> findAllById(List<Integer> ids) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteById(Integer id) {
		updateByExample(delete_sql_11, id);
	}

	public void deleteById2(Integer id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		updateByExample(delete_sql_12, paramMap);
	}

	@Override
	public void delete(User entity) {
		updateByExample(delete_sql_31, entity.getName(), entity.getAge(), entity.getBirthday());
	}

	public void delete2(User entity) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", entity.getName());
		paramMap.put("age", entity.getAge());
		paramMap.put("birthday", entity.getBirthday());
		updateByExample(delete_sql_32, paramMap);
	}

	@Override
	public void deleteAll(List<User> entities) {

	}

	@Override
	public void deleteAll() {
		updateByExample(delete_sql_2);
	}

	// ====================ExampleDao====================
	// queryForObject/queryForMap for single row
	// RowMapperResultSetExtractor & BeanPropertyRowMapper
	@Override
	public Optional<User> findByExample(String sql, Object... args) {
		return Optional.of(advancedJdbcTemplate.queryForObject(sql, args, User.class));
	}

	@Override
	public Optional<User> findByExample(String sql, Map<String, ?> namedParam) {
		return Optional.of(advancedJdbcTemplate.queryForObject(sql, namedParam, User.class));
	}

	// RowMapperResultSetExtractor & ColumnMapRowMapper
	@Override
	public Map<String, ?> findByExampleAsMap(String sql, Object... args) {
		return jdbcTemplate.queryForMap(sql, args);
	}

	@Override
	public Map<String, ?> findByExampleAsMap(String sql, Map<String, ?> namedParam) {
		return namedParameterJdbcTemplate.queryForMap(sql, namedParam);
	}

	// queryForList for multiple row
	// RowMapperResultSetExtractor & BeanPropertyRowMapper
	@Override
	public List<User> findAllByExample(String sql, Object... args) {
		// 可以用PreparedStatementSetter代替args
		return advancedJdbcTemplate.queryForObjectList(sql, args, User.class);
	}

	@Override
	public List<User> findAllByExample(String sql, Map<String, ?> namedParam) {
		return advancedJdbcTemplate.queryForObjectList(sql, namedParam, User.class);
	}

	// RowMapperResultSetExtractor & ColumnMapRowMapper
	@Override
	public List<Map<String, Object>> findAllByExampleAsMap(String sql, Object... args) {
		return jdbcTemplate.queryForList(sql, args);
	}

	@Override
	public List<Map<String, Object>> findAllByExampleAsMap(String sql, Map<String, ?> paramMap) {
		return namedParameterJdbcTemplate.queryForList(sql, paramMap);
	}

	@Override
	public int updateByExample(String sql, Object... args) {
		log.info("updateByExample(String sql, Object... args)");

		// 可以用PreparedStatementSetter代替args
		return jdbcTemplate.update(sql, args);
	}

	@Override
	public int updateByExample(String sql, Map<String, ?> paramMap) {
		log.info("updateByExample(String sql, Map<String, ?> paramMap)");

		return namedParameterJdbcTemplate.update(sql, paramMap);
	}

	@Override
	public int updateByExample2(String sql, Map<String, ?> paramMap) {
		log.info("updateByExample(String sql, Map<String, ?> paramMap)");
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(paramMap), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Deprecated
	@Override
	public int updateByExample(PreparedStatementCreator psc) {
		log.info("updateByExample(PreparedStatementCreator psc)");
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public int[] batchUpdateByExample(String sql, List<Object[]> batchArgs) {
		// 可以使用BatchPreparedStatementSetter代替batchArgs
		return jdbcTemplate.batchUpdate(sql, batchArgs);
	}

	@Override
	public int[] batchUpdateByExample(String sql, Map<String, ?>[] batchValues) {
		// SqlParameterSource[] batchArgs =
		// SqlParameterSourceUtils.createBatch(batchValues);
		// SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(list);
		// 可以使用batchArgs代替batchValues
		return namedParameterJdbcTemplate.batchUpdate(sql, batchValues);
	}

	@Override
	public <T> int[][] batchUpdateByExample(String sql, Collection<T> batchArgs, int batchSize,
			ParameterizedPreparedStatementSetter<T> ppss) {
		return jdbcTemplate.batchUpdate(sql, batchArgs, batchSize, ppss);
	}

	// ====================transactional====================
	// 不能在事务方法中进行try-catch
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { ArithmeticException.class })
	public void transactionalMethod(User... users) {
		createUser1(users[0]);
		createUser1(users[1]);

		transactionalSubMethod(users[2], users[3]);

		int i = 1 / 0;
	}

	// 不能在事务方法中进行try-catch
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { ArithmeticException.class })
	private void transactionalSubMethod(User... users) {
		createUser1(users[0]);
		createUser1(users[1]);
	}

	// ====================SimpleJdbc====================
	private void insertUser6(User user) {
		log.info("insertUser6(User user)");
		Map<String, Object> parameters = new HashMap<String, Object>(3);
		parameters.put("name", user.getName());
		parameters.put("age", user.getAge());
		parameters.put("birthday", user.getBirthday());
		simpleJdbcInsert.execute(parameters);
		Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
		log.info("generatedKey=" + newId.longValue());
	}

	private void callProcedure() {
		log.info("callProcedure()");
		SqlParameterSource in = new MapSqlParameterSource().addValue("in_id", 1);
		simpleJdbcCall.withProcedureName("user").withoutProcedureColumnMetaDataAccess().useInParameterNames("in_id")
				.declareParameters(new SqlParameter("in_id", Types.NUMERIC),
						new SqlOutParameter("out_first_name", Types.VARCHAR),
						new SqlOutParameter("out_last_name", Types.VARCHAR),
						new SqlOutParameter("out_birth_date", Types.DATE));
		Map<String, Object> out = simpleJdbcCall.execute(in);
		User user = new User();
		user.setName((String) out.get("out_first_name"));
		user.setAge((int) out.get("age"));
	}

	private void createTable() {
		log.info("createTable()");
		if (jdbcTemplate != null) {
			String sql = "create table user(id int(11) not null AUTO_INCREMENT,name varchar(128) not null default '',gender int(11) not null default 0,age int(4) not null default 0";
			jdbcTemplate.execute(sql);
		}
		throw new UnsupportedOperationException();
	}

	// ====================private====================
	// RowMapperResultSetExtractor & SingleColumnRowMapper
	private void queryAnything() {
		String select_any_sql_1 = "select name from user where id = ?";
		String select_any_sql_2 = "select name from user where id = :id";
		String select_any_sql_3 = "select name from user where id > ?";
		String select_any_sql_4 = "select name from user where id > :id";

		String column1 = jdbcTemplate.queryForObject(select_any_sql_1, new Object[] { 1 }, String.class);
		List<String> columnList1 = jdbcTemplate.queryForList(select_any_sql_3, new Object[] { 1 }, String.class);
		log.info("column1=" + column1);
		columnList1.forEach(column -> log.info("column=" + column));

		// MapSqlParameterSource
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", 1);
		String column2 = namedParameterJdbcTemplate.queryForObject(select_any_sql_2, paramMap, String.class);
		List<String> columnList2 = namedParameterJdbcTemplate.queryForList(select_any_sql_4, paramMap, String.class);
		log.info("column2=" + column2);
		columnList2.forEach(column -> log.info("column=" + column));
	}

	private int createUser1(User user) {
		log.info("createUser1(User user)" + user);

		return updateByExample(insert_sql_11, user.getName(), user.getAge(), user.getBirthday());
	}

	private int createUser2(User user) {
		log.info("createUser2(User user)" + user);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", user.getName());
		paramMap.put("age", user.getAge());
		paramMap.put("birthday", user.getBirthday());

		return updateByExample(insert_sql_12, paramMap);
	}

	private int createUser3(User user) {
		log.info("createUser3(User user)" + user);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", user.getName());
		paramMap.put("age", user.getAge());
		paramMap.put("birthday", user.getBirthday());

		return updateByExample2(insert_sql_12, paramMap);
	}

	private int createUser4(User user) {
		log.info("createUser4(User user)" + user);

		PreparedStatementCreator upsc = buildUserPSC(insert_sql_11, user);
		return updateByExample(upsc);
	}

	private int[] batchCreateUser1(List<User> users) {
		log.info("batchUpdateUser1(final List<User> users)");
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (User user : users) {
			Object[] values = new Object[] { user.getAge(), 1 };
			batchArgs.add(values);
		}
		return batchUpdateByExample(insert_sql_11, batchArgs);
	}

	private <T> int[] batchCreateUser2(List<User> users) {
		log.info("batchUpdateUser2(final Map<String, ?>... batchValues)");
		Map<String, Object>[] batchValues = new HashMap[users.size()];
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			batchValues[i].put("name", user.getName());
			batchValues[i].put("age", user.getAge());
			batchValues[i].put("birthday", user.getBirthday());
		}
		return batchUpdateByExample(insert_sql_12, batchValues);
	}

	private int[][] batchCreateUser3(List<User> users) {
		log.info("batchUpdateUser3(final List<User> users)");
		ParameterizedPreparedStatementSetter<User> uppss = buildUserPPSS();
		return batchUpdateByExample(insert_sql_11, users, 2, uppss);
	}

	private PreparedStatementSetter buildUserPSS(User user) {
		return ps -> {
			ps.setString(1, user.getName());
			ps.setInt(2, user.getAge());
			ps.setDate(3, user.getBirthday());
		};
	}

	private BatchPreparedStatementSetter buildUserBPSS(List<User> users) {
		return new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, users.get(i).getAge());
				ps.setString(2, users.get(i).getName());
			}

			@Override
			public int getBatchSize() {
				return users.size();
			}
		};
	}

	private ParameterizedPreparedStatementSetter<User> buildUserPPSS() {
		return (ps, arg) -> {
			ps.setString(1, arg.getName());
			ps.setInt(2, arg.getAge());
			ps.setDate(3, arg.getBirthday());
		};
	}

	private PreparedStatementCreator buildUserPSC(String sql, User user) {
		return conn -> {
			PreparedStatement ps = conn.prepareStatement(sql, new String[] { "id" });
			ps.setString(1, user.getName());
			ps.setInt(2, user.getAge());
			ps.setDate(3, user.getBirthday());
			return ps;
		};
	}

}
