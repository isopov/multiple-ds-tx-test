package com.sopovs.moradanen.ds1annotation;

import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@TransactionConfiguration
public class TestTxConfiguration {

	@Bean
	public DataSource ds() {
		JDBCDataSource jdbcDataSource = new JDBCDataSource();
		jdbcDataSource.setUrl("jdbc:hsqldb:mem:test1annotation");
		return jdbcDataSource;
	}

	@Bean
	public ITransationalService service() {
		return new TransationalService();
	}

	@Bean
	public NamedParameterJdbcTemplate template() {
		return new NamedParameterJdbcTemplate(ds());
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(ds());
	}

}
