/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ai.auditlog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.PreparedStatementBinder;
import org.springframework.cassandra.core.ResultSetExtractor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.DriverException;

/**
 *
 * @author Chandra
 */
@Repository
public class MessageFinder {
	
	private static final String SELECT_MESSAGE_BY_NAME = "SELECT payload_id,service_name, create_ts, soap_action FROM message WHERE service_name = :service_name";
	private static final String SELECT_MESSAGE_BY_DATE_RANGE = "SELECT payload_id,service_name, create_ts, soap_action FROM message WHERE service_name= :service_name and create_ts >= :start_ts and create_ts <= :end_ts";
	
	@Autowired
	private CassandraTemplate template;
	
	private ResultSetExtractor<List<Message>> resultSetExtractor = new ResultSetExtractor<List<Message>> (){
		
		@Override
		public List<Message> extractData(ResultSet rs) throws DriverException, DataAccessException {
			List<Message> messages = new ArrayList<>();
			Message msg;
			for(Row row: rs.all()){
				msg= new Message();
				msg.setId(row.getString("payload_id"));
				msg.setServiceName(row.getString("service_name"));
				msg.setTimeStamp(row.getDate("create_ts"));
				msg.setSoapAction(row.getString("soap_action"));
				messages.add(msg);
			}
			return messages;
		}};
	
	public List<Message> findMessageByServiceName(String serviceName, int start, int count) {
		return template.query(SELECT_MESSAGE_BY_NAME, new PreparedStatementBinder(){
			@Override
			public BoundStatement bindValues(PreparedStatement ps) throws DriverException {
				BoundStatement bound = ps.bind()
						.setString("service_name", serviceName);
				return bound;
			}},  resultSetExtractor);
	}

	public List<Message> findMessageByDateRange(String serviceName, Date startTimeStamp, Date endTimeStamp, int start,
			int count) {

		return template.query(SELECT_MESSAGE_BY_DATE_RANGE, new PreparedStatementBinder(){
			@Override
			public BoundStatement bindValues(PreparedStatement ps) throws DriverException {
				BoundStatement bound = ps.bind()
						.setString("service_name", serviceName)
						.setDate("start_ts", startTimeStamp)
						.setDate("end_ts", endTimeStamp);
				return bound;
			}},  resultSetExtractor);
	}

}
