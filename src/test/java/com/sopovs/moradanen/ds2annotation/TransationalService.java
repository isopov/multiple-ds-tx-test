package com.sopovs.moradanen.ds2annotation;

import java.util.Collections;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;


@Transactional
public class TransationalService implements ITransationalService {

	@Autowired
	@Qualifier("first")
	private NamedParameterJdbcTemplate first;

	@Autowired
	@Qualifier("second")
	private NamedParameterJdbcTemplate second;

	public void setUp() {
		first.update("create table test1(id int primary key)",
				Collections.<String, Object> emptyMap());
		second.update("create table test2(id int primary key)",
				Collections.<String, Object> emptyMap());
	}

	@Override
	public void testTransational() {
		first.update("insert into test1(id) values(:id)",
				new MapSqlParameterSource("id", 1));
        System.out.println("Action in first DS took place without taking connection from the second");
		second.update("insert into test2 values(:id)",
				new MapSqlParameterSource("id", 1));
		second.update("insert into test2 values(:id)",
				new MapSqlParameterSource("id", 1));
	}

	@Override
	public int countAll() {
		return first.queryForInt("select count(*) from test1",
				Collections.<String, Object> emptyMap())
				+ second.queryForInt("select count(*) from test2",
						Collections.<String, Object> emptyMap());
	}
}
