package org.ruanwei.demo.springframework;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig2 {
	
	@Bean
    public DataSource dataSource() {
        // instantiate, configure and return DataSource
		return null;
    }
}
