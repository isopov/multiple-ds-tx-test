package com.sopovs.moradanen.ds2annotation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestTxConfiguration.class)
public class TransactionalServiceTest {

	@Autowired
	private ITransationalService service;

	@Before
	public void setUp() {
		service.setUp();
	}

	@Test
	public void testTransactional() {
		try {
			service.testTransational();
			Assert.fail();
		} catch (DuplicateKeyException dkex) {
			// exopected
		}
		Assert.assertEquals(0, service.countAll());
	}

}
