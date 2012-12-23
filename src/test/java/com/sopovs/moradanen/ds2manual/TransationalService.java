package com.sopovs.moradanen.ds2manual;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.transaction.*;
import java.util.Collections;

public class TransationalService {


    private NamedParameterJdbcTemplate first;
    private NamedParameterJdbcTemplate second;
    private TransactionManager tm;

    public void setTm(TransactionManager tm) {
        this.tm = tm;
    }

    public void setSecond(NamedParameterJdbcTemplate second) {
        this.second = second;
    }

    public void setFirst(NamedParameterJdbcTemplate first) {

        this.first = first;
    }

    public void setUp() {
        first.update("create table test1(id int primary key)", Collections.<String, Object>emptyMap());
        second.update("create table test2(id int primary key)", Collections.<String, Object>emptyMap());
    }

    public void testTransational() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        tm.begin();
        try {
            first.update("insert into test1(id) values(:id)", new MapSqlParameterSource("id", 1));
            second.update("insert into test2(id) values(:id)", new MapSqlParameterSource("id", 1));
            second.update("insert into test2(id) values(:id)", new MapSqlParameterSource("id", 1));
            tm.commit();
        } catch (DuplicateKeyException dkex) {
            tm.rollback();
            throw dkex;
        }
    }

    public int countAll() {
        return first.queryForInt("select count(*) from test1", Collections.<String, Object>emptyMap())
                + second.queryForInt("select count(*) from test2", Collections.<String, Object>emptyMap());
    }
}
