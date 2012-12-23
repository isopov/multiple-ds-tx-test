package com.sopovs.moradanen.ds2annotation;

import java.sql.SQLException;

import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.h2.jdbcx.JdbcDataSource;
import org.hsqldb.jdbc.JDBCDataSource;
import org.hsqldb.jdbc.pool.JDBCXADataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;

@Configuration
@EnableTransactionManagement
public class TestTxConfiguration {

	@Bean
	@Qualifier("first")
	public DataSource firstDs() throws SQLException {
        JdbcDataSource secondDs = new JdbcDataSource();
        secondDs.setURL("jdbc:h2:mem:test2annotation1");

        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setXaDataSource(secondDs);
        ds.setUniqueResourceName("h2DS1");
        return ds;
	}

	@Bean
	@Qualifier("second")
	public DataSource secondDs() {
		JdbcDataSource secondDs = new JdbcDataSource();
		secondDs.setURL("jdbc:h2:mem:test2annotation2");

		AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
		ds.setXaDataSource(secondDs);
		ds.setUniqueResourceName("h2DS2");
		return ds;
	}

	@Bean
	public ITransationalService service() {
		return new TransationalService();
	}

	@Bean
	@Qualifier("first")
	public NamedParameterJdbcTemplate firstTemplate() throws SQLException {
		return new NamedParameterJdbcTemplate(firstDs());
	}

	@Bean
	@Qualifier("second")
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
