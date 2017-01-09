/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ai.message.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.soap.MimeHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ai.message.domain.Message;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.UDTValue;

/**
 *
 * @author Chandra
 */
@Repository
public class MessageFinder {
	
	private static final String SELECT_MESSAGE_BY_NAME = "SELECT payload_id,service_name, create_ts FROM message_by_created_ts WHERE service_name = :service_name";
	private static final String SELECT_MESSAGE_BY_DATE = "SELECT payload_id,service_name, create_ts FROM message_by_created_ts WHERE service_name = :service_name and create_ts = :create_ts";
	private static final String SELECT_MESSAGE_BY_ID = "SELECT payload_id,service_name, create_ts FROM message_by_created_ts WHERE service_name = :service_name and create_ts = :create_ts and payload_id= :payload_id";

	private static final String SELECT_MESSAGE_BY_DATE_RANGE = "SELECT payload_id,service_name, create_ts FROM message_by_created_ts WHERE service_name= :service_name and create_ts >= :start_ts and create_ts <= :end_ts";
	private static final String SELECT_MESSAGE_STATUS = "SELECT payload_id,status FROM message_by_payload_id WHERE payload_id in :payload_ids";
	private static final String SELECT_PAYLOAD_DATA = "SELECT mime_headers,payload_xml,status FROM message_by_payload_id WHERE payload_id = :payload_id";
	
	private Session session;
	
	@Autowired
	public void setSession(Session session) {
		this.session = session;
	}

	public List<Message> findMessageByServiceName(String serviceName, int start, int count) {
		Map<String, Object> values = new HashMap<>();
		values.put("service_name", serviceName);
		ResultSet rs = session.execute(SELECT_MESSAGE_BY_NAME, values);
		return readRows(rs, serviceName);
	}

	public List<Message> findMessageByDate(String serviceName, Date timeStamp) {
		Map<String, Object> values = new HashMap<>();
		values.put("service_name", serviceName);
		values.put("create_ts", timeStamp);
		ResultSet rs = session.execute(SELECT_MESSAGE_BY_DATE, values);
		return readRows(rs, serviceName);

	}

	public List<Message> findMessageByDateRange(String serviceName, Date startTimeStamp, Date endTimeStamp, int start,
			int count) {
		
		Map<String, Object> values = new HashMap<>();
		values.put("service_name", serviceName);
		values.put("start_ts", startTimeStamp);
		values.put("end_ts", endTimeStamp);
		
		ResultSet rs = session.execute(SELECT_MESSAGE_BY_DATE_RANGE, values);
		return readRows(rs, serviceName);
	}
	private Map<String, String> getMessageStatus(Set<String> payloadIds) {
		
		Map<String, String> messageStatus= new HashMap<>();
		Map<String, Object> values = new HashMap<>();
		values.put("payload_ids", payloadIds);
		
		ResultSet rs = session.execute(SELECT_MESSAGE_STATUS, values);
		for(Row row: rs.all()){
			messageStatus.put(row.getString("payload_id"), row.getString("status"));
		}
		return messageStatus;
	}
	private List<Message> readRows(ResultSet rs, String serviceName){
		Map<String, Message> messages = new HashMap<>();
		Message msg;
		for(Row row: rs.all()){
			msg= new Message();
			msg.setId(row.getString("payload_id"));
			msg.setServiceName(serviceName);
			msg.setTimeStamp(row.getTimestamp("create_ts"));
			messages.put(msg.getId(),msg);
		}
		Map<String, String> messageStatus = getMessageStatus(messages.keySet());
		messages.keySet().stream().forEach(e->{ messages.get(e).setStatus(messageStatus.get(e));});		
		return messages.values().stream().collect(Collectors.toList());
		
	}

	public Message findMessageById(String serviceName, Date timeStamp, String payloadId) {
		Map<String, Object> values = new HashMap<>();
		values.put("service_name", serviceName);
		values.put("create_ts", timeStamp);
		values.put("payload_id", payloadId);
		ResultSet rs = session.execute(SELECT_MESSAGE_BY_ID, values);
		Row row=  rs.one();
		Message  msg= new Message();
		msg.setId(row.getString("payload_id"));
		msg.setServiceName(serviceName);
		msg.setTimeStamp(row.getTimestamp("create_ts"));
		readPayload(msg);
		return msg;
	}
	
	public Message findMessageById(String payloadId) {
		Message  msg= new Message();
		msg.setId(payloadId);
		readPayload(msg);
		return msg;
	}
	
	private void readPayload(Message message) {
		Map<String, Object> values = new HashMap<>();
		values.put("payload_id", message.getId());
		ResultSet rs = session.execute(SELECT_PAYLOAD_DATA, values);
		Row row= rs.one();
		message.setStatus(row.getString("status"));
		List<UDTValue> headers = row.getList("mime_headers", UDTValue.class);
		MimeHeaders mimeHeaders = new MimeHeaders();
		headers.stream().forEach(e -> mimeHeaders.addHeader(e.getString("name"), e.getString("value")));
		message.setMimeHeaders(mimeHeaders);
		message.setMessage(row.getBytes("payload_xml").array());
	}
	
	

}
