/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ai.message.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ai.message.domain.Message;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 *
 * @author Chandra
 */
@Repository
public class MessageFinder {
	
	private static final String SELECT_MESSAGE_BY_NAME = "SELECT payload_id,service_name, create_ts, soap_action,mime_headers FROM message WHERE service_name = :service_name";
	private static final String SELECT_MESSAGE_BY_DATE = "SELECT payload_id,service_name, create_ts, soap_action,mime_headers FROM message WHERE service_name = :service_name and create_ts = :create_ts";
	private static final String SELECT_MESSAGE_BY_DATE_RANGE = "SELECT payload_id,service_name, create_ts, soap_action,mime_headers FROM message WHERE service_name= :service_name and create_ts >= :start_ts and create_ts <= :end_ts";
	
	private Session session;
	
	@Autowired
	public void setSession(Session session) {
		this.session = session;
	}

	public List<Message> findMessageByServiceName(String serviceName, int start, int count) {
		List<Message> messages = new ArrayList<>();
		Message msg;
		Map<String, Object> values = new HashMap<>();
		values.put("service_name", serviceName);
		ResultSet rs = session.execute(SELECT_MESSAGE_BY_NAME, values);
		for(Row row: rs.all()){
			msg= new Message();
			msg.setId("c " +row.getString("payload_id"));
			msg.setServiceName(serviceName);
			msg.setTimeStamp(row.getTimestamp("create_ts"));
			msg.setSoapAction(row.getString("soap_action"));
			msg.setMimeHeaders(row.getMap("mime_headers",String.class, String.class));
			messages.add(msg);
		}
		return messages;
	}

	public Message findMessageByDate(String serviceName, Date timeStamp) {
		Message msg = null;
		Map<String, Object> values = new HashMap<>();
		values.put("service_name", serviceName);
		values.put("create_ts", timeStamp);
		
		ResultSet rs = session.execute(SELECT_MESSAGE_BY_DATE, values);
		Row row = rs.one();
		if(row!=null){
			msg= new Message();
			msg.setId(row.getString("payload_id"));
			msg.setServiceName(serviceName);
			msg.setTimeStamp(row.getTimestamp("create_ts"));
			msg.setSoapAction(row.getString("soap_action"));
			msg.setMimeHeaders(row.getMap("mime_headers",String.class, String.class));
		}
		return msg;

	}

	public List<Message> findMessageByDateRange(String serviceName, Date startTimeStamp, Date endTimeStamp, int start,
			int count) {
		
		List<Message> messages = new ArrayList<>();
		Message msg;
		Map<String, Object> values = new HashMap<>();
		values.put("service_name", serviceName);
		values.put("start_ts", startTimeStamp);
		values.put("end_ts", endTimeStamp);
		
		ResultSet rs = session.execute(SELECT_MESSAGE_BY_DATE_RANGE, values);
		for(Row row: rs.all()){
			msg= new Message();
			msg.setId(row.getString("payload_id"));
			msg.setServiceName(serviceName);
			msg.setTimeStamp(row.getTimestamp("create_ts"));
			msg.setSoapAction(row.getString("soap_action"));
			msg.setMimeHeaders(row.getMap("mime_headers",String.class, String.class));
			messages.add(msg);
		}
		return messages;
	}

}
