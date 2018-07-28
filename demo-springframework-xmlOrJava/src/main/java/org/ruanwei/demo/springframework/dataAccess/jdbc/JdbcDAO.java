package org.ruanwei.demo.springframework.dataAccess.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import org.springframework.jdbc.core.RowMapper;
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
 * 
 * @author ruanwei
 *
 */
public class JdbcDAO{
	private static Log log = LogFactory.getLog(JdbcDAO.class);
	
	private DataSource dataSource;
	
	// thread-safe
	private JdbcTemplate jdbcTemplate; 
	// rovide named parameters instead of the traditional JDBC "?" placeholders.
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	// Simplifying JDBC operations with the SimpleJdbc classes
	private SimpleJdbcInsert simpleJdbcInsert;
	private SimpleJdbcCall simpleJdbcCall;// 执行存储过程或者函数

	// Modeling JDBC operations as Java objects.
	private SqlQuery<User> sqlQuery;
	private MappingSqlQuery<User> mappingSqlQuery;
	private UpdatableSqlQuery<User> updatableSqlQuery;
	private SqlUpdate sqlUpdate;
	private StoredProcedure storedProcedure;
	
	// RowCallbackHandler

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				dataSource);
		this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(
				"user").usingGeneratedKeyColumns("id");
		this.simpleJdbcCall = new SimpleJdbcCall(dataSource);
	}

	public int countAll() {
		log.info("countAll()");
		int rowCount = jdbcTemplate.queryForObject("select count(*) from user",
				Integer.class);

		// jmsTemplate.send(activeMQQueue, new MessageCreator() {
		// @Override
		// public Message createMessage(Session session) throws JMSException {
		// return session.createTextMessage("Hello, JMS!");
		// }
		// });

		return rowCount;
	}

	public String findNameById(int id) {
		log.info("findNameById(int id)");
		String sql = "select name from user where id = ?";
		String name = jdbcTemplate.queryForObject(sql, new Object[] { 1L },
				String.class);
		return name;
	}

	public String findNameById2(int id) {
		log.info("findNameById2(int id)");
		String sql = "select name from user where id = :id";
		// SqlParameterSource namedParameters = new
		// BeanPropertySqlParameterSource(exampleActor)
		// Map<String, String> namedParameters = Collections.singletonMap("id",
		// 1);
		SqlParameterSource namedParameters = new MapSqlParameterSource("id", 1);
		String name = namedParameterJdbcTemplate.queryForObject(sql,
				namedParameters, String.class);
		return name;
	}

	public User findUserById(int id) {
		log.info("findUserById(int id)");
		String sql = "select id, name, age from user where id = ?";
		User user = jdbcTemplate.queryForObject(sql, new Object[] { 1L },
				new RowMapper<User>() {
					@Override
					public User mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						User user = new User(rs.getString("name"), rs
								.getInt("age"));
						log.info("user=" + user + " id=" + rs.getInt("id"));
						return user;
					}
				});
		log.info("user=" + user);
		return user;
	}

	public List<String> findNameList() {
		log.info("findNameList()");
		if (jdbcTemplate != null) {
			String sql = "select name from user";
			// 注意，这里只能返回单列的List，对比findUserList().
			List<String> list = jdbcTemplate.queryForList(sql, String.class);
			for (String name : list) {
				log.info("name=" + name);
			}
			return list;
		}
		throw new UnsupportedOperationException();
	}

	public List<User> findUserList() {
		log.info("findUserList()");
		if (jdbcTemplate != null) {
			String sql = "select * from user";
			List<User> list = jdbcTemplate.query(sql, new RowMapper<User>() {
				@Override
				public User mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					User user = new User(rs.getString("name"), rs.getInt("age"));
					log.info("user=" + user + " id=" + rs.getInt("id"));
					return user;
				}
			});
			return list;
		}
		throw new UnsupportedOperationException();
	}

	public void findUserList2() {
		log.info("findUserList2()");
		if (jdbcTemplate != null) {
			String sql = "select * from user";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			for (Map<String, Object> map : list) {
				String output = String.format("id=%d, name=%s, gender=%b",
						map.get("id"), map.get("name"), map.get("gender"));
				log.info(output);
			}
		}
		throw new UnsupportedOperationException();
	}

	public int insertUser(User user) {
		log.info("insertUser(User user)");
		if (jdbcTemplate != null) {
			String sql = "insert into user(name,age,birthday) values(?,?,?)";
			int count = jdbcTemplate.update(sql, "ruanwei", 28, "1983-07-06");
			return count;
		}
		throw new UnsupportedOperationException();
	}

	public int insertUser2(User user) {
		log.info("insertUser2(User user)");
		if (jdbcTemplate != null) {
			String sql = "insert into user(name,age,birthday) values(?,?,?)";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			int count = jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql,
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

	public int insertUser3(User user) {
		log.info("insertUser3(User user)");
		if (jdbcTemplate != null) {
			String sql = "insert into user(name,age,birthday) values(?,?,?)";
			int count = jdbcTemplate.update(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, "ruanwei");
					ps.setInt(2, 28);
					ps.setDate(3, new Date(1983, 7, 6));
				}
			});
			return count;
		}
		throw new UnsupportedOperationException();
	}

	public void insertUser4(User user) {
		log.info("insertUser4(User user)");
		if (simpleJdbcInsert != null) {
			Map<String, Object> parameters = new HashMap<String, Object>(3);
			parameters.put("name", "ruanwei");
			parameters.put("age", 28);
			parameters.put("birthday", new Date(1983, 7, 6));
			simpleJdbcInsert.execute(parameters);
			Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
			log.info("generatedKey=" + newId.longValue());
		}
		throw new UnsupportedOperationException();
	}

	public int updateUser(User user) {
		log.info("updateUser(User user)");
		if (jdbcTemplate != null) {
			String sql = "update user set age=? where id!=?	";
			int count = jdbcTemplate.update(sql, 28, 1);
			return count;
		}
		throw new UnsupportedOperationException();
	}

	public int[] batchUpdateUser(final List<User> users) {
		log.info("batchUpdateUser(final List<User> users)");
		if (jdbcTemplate != null) {
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
		throw new UnsupportedOperationException();
	}

	public int[][] batchUpdateUser2(final Collection<User> users) {
		log.info("batchUpdateUser2(final List<User> users)");
		if (jdbcTemplate != null) {
			String sql = "update user set age=? where id!=?";
			int[][] updateCounts = jdbcTemplate.batchUpdate(sql, users, 100,
					new ParameterizedPreparedStatementSetter<User>() {
						public void setValues(PreparedStatement ps,
								User argument) throws SQLException {
							ps.setInt(1, argument.getAge() + 10);
							ps.setInt(2, 1);
						}
					});
			return updateCounts;
		}
		throw new UnsupportedOperationException();
	}

	public int[] batchUpdateUser3(final List<User> users) {
		log.info("batchUpdateUser3(final List<User> users)");
		if (namedParameterJdbcTemplate != null) {
			String sql = "update user set age=:age where id!=:id";
			SqlParameterSource[] batch = SqlParameterSourceUtils
					.createBatch(users.toArray());
			int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql,
					batch);
			return updateCounts;
		}
		throw new UnsupportedOperationException();
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

	public int deleteUser(int id) {
		log.info("deleteUser(User user)");
		if (jdbcTemplate != null) {
			String sql = "delete from user where id=?";
			int count = jdbcTemplate.update(sql, id);
			return count;
		}
		throw new UnsupportedOperationException();
	}

	public void callProcedure() {
		log.info("callProcedure()");
		if (simpleJdbcCall != null) {
			SqlParameterSource in = new MapSqlParameterSource().addValue(
					"in_id", 1);
			simpleJdbcCall
					.withProcedureName("user")
					.withoutProcedureColumnMetaDataAccess()
					.useInParameterNames("in_id")
					.declareParameters(
							new SqlParameter("in_id", Types.NUMERIC),
							new SqlOutParameter("out_first_name", Types.VARCHAR),
							new SqlOutParameter("out_last_name", Types.VARCHAR),
							new SqlOutParameter("out_birth_date", Types.DATE));
			Map out = simpleJdbcCall.execute(in);
			User user = new User();
			user.setName((String) out.get("name"));
			user.setAge((int) out.get("age"));
		}
		throw new UnsupportedOperationException();
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
