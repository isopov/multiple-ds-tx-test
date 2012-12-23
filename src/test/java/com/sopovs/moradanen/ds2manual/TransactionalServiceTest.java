package com.sopovs.moradanen.ds2manual;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.*;

public class TransactionalServiceTest {

	private TransationalService service;

    @Before
	public void setUp() throws SystemException {
        service = new TransationalService();

        UserTransactionManager  utm = new UserTransactionManager();
        utm.init();

        JdbcDataSource h2ds1 = new JdbcDataSource();
        h2ds1.setURL("jdbc:h2:mem:test2manual1");

        AtomikosDataSourceBean ds1 = new AtomikosDataSourceBean();
        ds1.setXaDataSource(h2ds1);
        ds1.setUniqueResourceName("test2Manual1");

        JdbcDataSource h2ds2 = new JdbcDataSource();
        h2ds2.setURL("jdbc:h2:mem:test2manual2");

        AtomikosDataSourceBean ds2 = new AtomikosDataSourceBean();
        ds2.setXaDataSource(h2ds2);
        ds2.setUniqueResourceName("test2Manual2");


        service = new TransationalService();
        service.setFirst(new NamedParameterJdbcTemplate(ds1));
        service.setSecond(new NamedParameterJdbcTemplate(ds2));
        service.setTm(utm);


		service.setUp();
	}

	@Test
	public void testTransactional() throws Exception {
		try {
			service.testTransational();
			Assert.fail();
		} catch (DuplicateKeyException dkex) {
			// exopected
		}
		Assert.assertEquals(0, service.countAll());
	}

}
