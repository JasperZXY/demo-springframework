package org.ruanwei.demo.springframework.dataAccess;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;

public interface ExampleDao<T> {

	Optional<T> findByExample(String sql, Object... args);

	Optional<T> findByExample(String sql, Map<String, ?> namedParam);

	public List<T> findAllByExample(String sql, Object... args);

	public List<T> findAllByExample(String sql, Map<String, ?> paramMap);

	public Map<String, ?> findByExampleAsMap(String sql, Object... args);

	public Map<String, ?> findByExampleAsMap(String sql, Map<String, ?> namedParam);

	public List<Map<String, Object>> findAllByExampleAsMap(String sql, Object... args);

	public List<Map<String, Object>> findAllByExampleAsMap(String sql, Map<String, ?> paramMap);

	public int updateByExample(String sql, Object... args);

	public int updateByExample(String sql, Map<String, ?> paramMap);

	public int updateByExample2(String sql, Map<String, ?> paramMap);

	@Deprecated
	public int updateByExample(PreparedStatementCreator psc);

	public int[] batchUpdateByExample(String sql, List<Object[]> batchArgs);

	public <T> int[][] batchUpdateByExample(String sql, final Collection<T> batchArgs, final int batchSize,
			final ParameterizedPreparedStatementSetter<T> pss);

	public int[] batchUpdateByExample(String sql, Map<String, ?>[] batchValues);

}
