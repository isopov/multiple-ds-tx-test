package com.sopovs.moradanen.ds1annotation;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TransactionalService implements ITransactionalService {

	@Autowired
	private NamedParameterJdbcTemplate t;

	@Override
	public void setUp() {
		t.update("create table test(id int primary key)", Collections.<String, Object> emptyMap());
	}

	@Override
	public void testTransational() {
		t.update("insert into test(id) values(:id)", new MapSqlParameterSource("id", 1));
		t.update("insert into test values(:id)", new MapSqlParameterSource("id", 1));
	}

	@Override
	public int countAll() {
		return t.queryForInt("select count(*) from test", Collections.<String, Object> emptyMap());
	}
}
