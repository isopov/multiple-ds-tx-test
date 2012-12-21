package com.sopovs.moradanen.ds2annotation;

import java.sql.SQLException;

import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.h2.jdbcx.JdbcDataSource;
import org.hsqldb.jdbc.pool.JDBCXADataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;

@Configuration
@TransactionConfiguration
public class TestTxConfiguration {

	@Bean
	@Qualifier("hsql")
	public DataSource firstDs() throws SQLException {
		JDBCXADataSource firstDs = new JDBCXADataSource();
		firstDs.setUrl("jdbc:hsqldb:mem:test2annotation");
		AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
		ds.setXaDataSource(firstDs);
		ds.setUniqueResourceName("hsqlDS");
		return ds;
	}

	@Bean
	@Qualifier("h2")
	public DataSource secondDs() {
		JdbcDataSource secondDs = new JdbcDataSource();
		secondDs.setURL("jdbc:h2:mem:test2annotation");

		AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
		ds.setXaDataSource(secondDs);
		ds.setUniqueResourceName("h2DS");
		return ds;
	}

	@Bean
	public ITransationalService service() {
		return new TransationalService();
	}

	@Bean
	@Qualifier("hsql")
	public NamedParameterJdbcTemplate firstTemplate() throws SQLException {
		return new NamedParameterJdbcTemplate(firstDs());
	}

	@Bean
	@Qualifier("h2")
	public NamedParameterJdbcTemplate secondTemplate() {
		return new NamedParameterJdbcTemplate(secondDs());
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager() throws SystemException {
		UserTransactionManager userTransactionManager = new UserTransactionManager();
		userTransactionManager.init();
		return new JtaTransactionManager((TransactionManager) userTransactionManager);
	}

}
