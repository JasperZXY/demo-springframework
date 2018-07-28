package org.ruanwei.demo.springframework.dataAccess.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.dataAccess.User;
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
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.jdbc.object.UpdatableSqlQuery;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * JdbcDaoSupport提供了setDataSource支持
 * 
 * @author ruanwei
 *
 */
public class JdbcDAO/** extends JdbcDaoSupport */
{
	private static Log log = LogFactory.getLog(JdbcDAO.class);

	// 1.core JdbcTemplate & NamedParameterJdbcTemplate thread-safe
	private JdbcTemplate jdbcTemplate;
	// rovide named parameters instead of the traditional JDBC "?" placeholders.
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

	private static final String sql_11 = "select name from user where id = 1";
	private static final String sql_12 = "select name from user where id = ?";
	private static final String sql_13 = "select name from user where id = :id";

	private static final String sql_21 = "select name, age from user where id = 1";
	private static final String sql_22 = "select name, age from user where id = ?";
	private static final String sql_23 = "select name, age from user where id = :id";

	private static final String sql_31 = "select * from user where id = 1";
	private static final String sql_32 = "select * from user where id = ?";
	private static final String sql_33 = "select * from user where id = :id";

	private static final String sql_41 = "select name from user where id > 0";
	private static final String sql_42 = "select name from user where id > ?";
	private static final String sql_43 = "select name from user where id > :id";

	private static final String sql_51 = "select name, age from user where id > 0";
	private static final String sql_52 = "select name, age from user where id > ?";
	private static final String sql_53 = "select name, age from user where id > :id";

	private static final String sql_61 = "select * from user where id > 0";
	private static final String sql_62 = "select * from user where id > ?";
	private static final String sql_63 = "select * from user where id > :id";

	private static final String sql_71 = "insert into user(name,age,birthday) values(\"ruanwei\", 28)";
	private static final String sql_72 = "insert into user(name,age,birthday) values(?, ?, ?)";
	private static final String sql_73 = "insert into user(name,age,birthday) values(:name, :age, :birthday)";

	private static final Object[] args0 = new Object[] { 0L };
	private static final Object[] args1 = new Object[] { 1L };
	private static final PreparedStatementSetter pss0 = ps -> ps.setLong(1, 0L);
	private static final PreparedStatementSetter pss1 = ps -> ps.setLong(1, 1L);
	private static final Map<String, Long> namedParam0 = new HashMap<String, Long>();
	private static final Map<String, Long> namedParam1 = new HashMap<String, Long>();
	private static final MapSqlParameterSource namedParam2 = new MapSqlParameterSource();

	static {
		namedParam0.put("id", 0L);
		namedParam1.put("id", 1L);

		namedParam2.addValue("name", "ruanwei");
		namedParam2.addValue("age", 28);
		namedParam2.addValue("birthday", new Date(1983, 7, 6));
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				dataSource);
		this.advancedJdbcTemplate = new AdvancedJdbcTemplate(jdbcTemplate,
				namedParameterJdbcTemplate);

		this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(
				"user").usingGeneratedKeyColumns("id");
		this.simpleJdbcCall = new SimpleJdbcCall(dataSource);
	}

	// ====================single row====================
	// RowMapperResultSetExtractor & SingleColumnRowMapper
	public void queryForSingleColumn() {
		String column1 = jdbcTemplate.queryForObject(sql_11, String.class);
		String column2 = jdbcTemplate.queryForObject(sql_12, args1,
				String.class);
		String column3 = namedParameterJdbcTemplate.queryForObject(sql_13,
				namedParam1, String.class);
		log.info("column1=" + column1 + ",column2=" + column2 + ",column3="
				+ column3);
	}

	// RowMapperResultSetExtractor & ColumnMapRowMapper
	public void queryForMultiColumn() {
		Map<String, Object> columnMap1 = jdbcTemplate.queryForMap(sql_21);
		Map<String, Object> columnMap2 = jdbcTemplate
				.queryForMap(sql_22, args1);
		Map<String, Object> columnMap3 = namedParameterJdbcTemplate
				.queryForMap(sql_23, namedParam1);
		columnMap1.forEach((k, v) -> log.info(k + "=" + v));
		columnMap2.forEach((k, v) -> log.info(k + "=" + v));
		columnMap3.forEach((k, v) -> log.info(k + "=" + v));
	}

	// RowMapperResultSetExtractor & BeanPropertyRowMapper
	public void queryForObject() {
		User obj1 = advancedJdbcTemplate.queryForObject(sql_31, User.class);
		User obj2 = advancedJdbcTemplate.queryForObject(sql_32, args1,
				User.class);
		User obj3 = advancedJdbcTemplate.queryForObject(sql_33, namedParam1,
				User.class);
		log.info("obj1=" + obj1 + ",obj2=" + obj2 + ",obj3=" + obj3);
	}

	// ====================multiple row====================
	public void queryForSingleColumnList() {
		List<String> columnList1 = jdbcTemplate.queryForList(sql_41,
				String.class);
		List<String> columnList2 = jdbcTemplate.queryForList(sql_42, args0,
				String.class);
		List<String> columnList3 = namedParameterJdbcTemplate.queryForList(
				sql_43, namedParam0, String.class);
		columnList1.forEach(column -> log.info("column=" + column));
		columnList2.forEach(column -> log.info("column=" + column));
		columnList3.forEach(column -> log.info("column=" + column));
	}

	public void queryForMultiColumnList() {
		List<Map<String, Object>> columnMapList1 = jdbcTemplate
				.queryForList(sql_51);
		List<Map<String, Object>> columnMapList2 = jdbcTemplate.queryForList(
				sql_52, args0);
		List<Map<String, Object>> columnMapList3 = jdbcTemplate.queryForList(
				sql_53, pss0);
		List<Map<String, Object>> columnMapList4 = namedParameterJdbcTemplate
				.queryForList(sql_53, namedParam0);

		columnMapList1.forEach(columbMap -> columbMap.forEach((k, v) -> log
				.info(k + "=" + v)));
		columnMapList2.forEach(columbMap -> columbMap.forEach((k, v) -> log
				.info(k + "=" + v)));
		columnMapList3.forEach(columbMap -> columbMap.forEach((k, v) -> log
				.info(k + "=" + v)));
		columnMapList4.forEach(columbMap -> columbMap.forEach((k, v) -> log
				.info(k + "=" + v)));
	}

	public void queryForObjectList() {
		List<User> objList1 = advancedJdbcTemplate.queryForObjectList(sql_61,
				User.class);
		List<User> objList2 = advancedJdbcTemplate.queryForObjectList(sql_62,
				args0, User.class);
		List<User> objList3 = advancedJdbcTemplate.queryForObjectList(sql_63,
				pss0, User.class);
		List<User> objList4 = advancedJdbcTemplate.queryForObjectList(sql_63,
				namedParam0, User.class);
		objList1.forEach(obj -> log.info("obj=" + obj));
		objList2.forEach(obj -> log.info("obj=" + obj));
		objList3.forEach(obj -> log.info("obj=" + obj));
		objList4.forEach(obj -> log.info("obj=" + obj));
	}

	// ====================update====================
	public int insertUser() {
		log.info("insertUser(User user)");
		int count = jdbcTemplate.update(sql_72, "ruanwei", 28, "1983-07-06");
		return count;
	}

	public int insertUser2() {
		log.info("insertUser2(User user)");
		int count = jdbcTemplate.update(sql_72, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, "ruanwei");
				ps.setInt(2, 28);
				ps.setDate(3, new Date(1983, 7, 6));
			}
		});
		return count;
	}

	public int insertUser3() {
		log.info("insertUser3(User user)");
		if (jdbcTemplate != null) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			int count = jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql_72,
							new String[] { "id" });
					ps.setString(1, "ruanwei");
					ps.setInt(2, 28);
					ps.setDate(3, new Date(1983, 7, 6));
					return ps;
				}
			}, keyHolder);
			log.info("generatedKey=" + keyHolder.getKey().longValue());
			return count;
		}
		throw new UnsupportedOperationException();
	}

	public int insertUser4() {
		log.info("insertUser4(User user)");
		int count = namedParameterJdbcTemplate.update(sql_73, namedParam2);
		return count;
	}

	public int insertUser5() {
		log.info("insertUser5(User user)");
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int count = namedParameterJdbcTemplate.update(sql_73, namedParam2,
				keyHolder);
		log.info("generatedKey=" + keyHolder.getKey().longValue());
		return count;
	}

	public int[] batchUpdateUser(final List<User> users) {
		log.info("batchUpdateUser(final List<User> users)");
		String sql = "update user set age=? where id!=?";
		int[] updateCounts = jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						ps.setInt(1, users.get(i).getAge() + 10);
						ps.setInt(2, 1);
					}

					@Override
					public int getBatchSize() {
						return users.size();
					}
				});
		return updateCounts;
	}

	public int[][] batchUpdateUser2(final Collection<User> users) {
		log.info("batchUpdateUser2(final List<User> users)");
		String sql = "update user set age=? where id!=?";
		int[][] updateCounts = jdbcTemplate.batchUpdate(sql, users, 10,
				new ParameterizedPreparedStatementSetter<User>() {
					public void setValues(PreparedStatement ps, User argument)
							throws SQLException {
						ps.setInt(1, argument.getAge() + 10);
						ps.setInt(2, 1);
					}
				});
		return updateCounts;
	}

	public int[] batchUpdateUser3(final List<User> users) {
		log.info("batchUpdateUser3(final List<User> users)");
		String sql = "update user set age=:age where id!=:id";
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(users
				.toArray());
		int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batch);
		return updateCounts;
	}

	public int[] batchUpdateUser4(final List<User> users) {
		log.info("batchUpdateUser4(final List<User> users)");
		if (jdbcTemplate != null) {
			String sql = "update user set age=:age where id!=:id";
			List<Object[]> batch = new ArrayList<Object[]>();
			for (User user : users) {
				Object[] values = new Object[] { user.getAge(), 1 };
				batch.add(values);
			}
			int[] updateCounts = jdbcTemplate.batchUpdate(sql, batch);
			return updateCounts;
		}
		throw new UnsupportedOperationException();
	}

	// ====================SimpleJdbc====================
	public void insertUser6() {
		log.info("insertUser6(User user)");
		Map<String, Object> parameters = new HashMap<String, Object>(3);
		parameters.put("name", "ruanwei");
		parameters.put("age", 28);
		parameters.put("birthday", new Date(1983, 7, 6));
		simpleJdbcInsert.execute(parameters);
		Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
		log.info("generatedKey=" + newId.longValue());
	}

	public void callProcedure() {
		log.info("callProcedure()");
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("in_id", 1);
		simpleJdbcCall
				.withProcedureName("user")
				.withoutProcedureColumnMetaDataAccess()
				.useInParameterNames("in_id")
				.declareParameters(new SqlParameter("in_id", Types.NUMERIC),
						new SqlOutParameter("out_first_name", Types.VARCHAR),
						new SqlOutParameter("out_last_name", Types.VARCHAR),
						new SqlOutParameter("out_birth_date", Types.DATE));
		Map<String, Object> out = simpleJdbcCall.execute(in);
		User user = new User();
		user.setName((String) out.get("out_first_name"));
		user.setAge((int) out.get("age"));
	}

	public void createTable() {
		log.info("createTable()");
		if (jdbcTemplate != null) {
			String sql = "create table user(id int(11) not null AUTO_INCREMENT,name varchar(128) not null default '',gender int(11) not null default 0,age int(4) not null default 0";
			jdbcTemplate.execute(sql);
		}
		throw new UnsupportedOperationException();
	}

}
