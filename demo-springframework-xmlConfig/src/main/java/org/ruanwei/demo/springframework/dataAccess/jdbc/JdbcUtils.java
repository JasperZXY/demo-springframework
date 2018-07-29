package org.ruanwei.demo.springframework.dataAccess.jdbc;

import java.util.Map;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

public class JdbcUtils {

	public static <T> SqlParameterSource create(T candidate) {
		SqlParameterSource[] batch = SqlParameterSourceUtils
				.createBatch(candidate);
		return batch[0];
	}

	public static SqlParameterSource create(Map<String, ?> valueMaps) {
		SqlParameterSource[] batch = SqlParameterSourceUtils
				.createBatch(valueMaps);
		return batch[0];
	}

}
