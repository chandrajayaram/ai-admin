/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ai.message.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.SOAPException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ai.message.domain.Message;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.UDTValue;
import com.datastax.driver.core.UserType;

/**
 *
 * @author Chandra
 */
@Repository
public class MessageManager {

	private PreparedStatement insertPreparedStmtByTime;
	private PreparedStatement insertPreparedStmtById;
	private PreparedStatement deletePreparedStmt;
	private PreparedStatement purgePreparedStmt;
	private PreparedStatement purgeAllPreparedStmt;
	private PreparedStatement purgeAllBySericvePreparedStmt; 
	
	private static final String INSERT_MESSAGE_BY_TIME="INSERT INTO message_by_created_ts(service_name, create_ts, payload_id) values (:service_name, :create_ts, :payload_id)";
	private static final String INSERT_MESSAGE_BY_ID="INSERT INTO message_by_payload_id(payload_id,status,mime_headers,payload_xml) values (:payload_id,:status,:mime_headers,:payload_xml)";
	
	private static final String DELETE_MESSAGE="DELETE FROM message_by_created_ts WHERE service_name = :service_name and create_ts = :create_ts";
	private static final String PURGE_MESSAGE="DELETE FROM message_by_created_ts WHERE service_name = :service_name and create_ts >= :start_ts and create_ts <= : end_ts";
	private static final String PURGE_MESSAGE_BY_SERVICE="DELETE FROM message_by_created_ts WHERE service_name = :service_name";
	
	private static final String PURGE_ALL_MESSAGES="";
	
	private Session session;
	
	@Autowired
	private UserType mimeType;
	
	@Autowired
	public void setSession(Session session) {
		this.session = session;
	}

	@PostConstruct
	public void init(){
		insertPreparedStmtByTime = session.prepare(INSERT_MESSAGE_BY_TIME);
		insertPreparedStmtById = session.prepare(INSERT_MESSAGE_BY_ID);
		deletePreparedStmt = session.prepare(DELETE_MESSAGE);
		purgePreparedStmt = session.prepare(PURGE_MESSAGE);
		purgeAllBySericvePreparedStmt = session.prepare(PURGE_MESSAGE_BY_SERVICE); 
		//purgeAllPreparedStmt = session.prepare(PURGE_MESSAGE);
		
	}
	
	public void create(Message message) throws SOAPException, IOException {
		BoundStatement bound = insertPreparedStmtByTime.bind()
				.setString("service_name", message.getServiceName())
				.setTimestamp("create_ts", message.getTimeStamp())
				.setString("payload_id", message.getId());
		session.execute(bound);
		@SuppressWarnings("unchecked")
		Iterator<MimeHeader> mimeHeaderIt = message.getMimeHeaders().getAllHeaders();
		List<UDTValue> headers = new ArrayList<>();
		while(mimeHeaderIt.hasNext()){
			MimeHeader header = mimeHeaderIt.next();
			UDTValue value = mimeType.newValue()
								.setString("name", header.getName())
								.setString("value", header.getValue());
			headers.add(value);
		}
		ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
		message.getSoapMessage().writeTo(outStream);
		session.execute(insertPreparedStmtById.bind()
						.setString("payload_id", message.getId())
						.setString("status", "new")
						.setList("mime_headers", headers)
						.setBytes("payload_xml", ByteBuffer.wrap(outStream.toByteArray())));
	}

	public void delete(String serviceName, Date timeStamp) {
		BoundStatement bound = deletePreparedStmt.bind()
				.setString("service_name", serviceName)
				.setTimestamp("create_ts", timeStamp);
		session.execute(bound);
	}

	public void purgeMessages(String serviceName, Date startTimeStamp, Date endTimeStamp) {
		BoundStatement bound = purgePreparedStmt.bind()
				.setString("service_name", serviceName)
				.setTimestamp("start_ts", startTimeStamp)
				.setTimestamp("end_ts", endTimeStamp);
		session.execute(bound);
	}

	public void purgeAllMessage(Date startTimeStamp, Date endTimeStamp) {
		BoundStatement bound = purgeAllPreparedStmt.bind()
				.setTimestamp("start_ts", startTimeStamp)
				.setTimestamp("end_ts", endTimeStamp);
		session.execute(bound);
	}
	public void purgeAllMessage(String serviceName) {
		BoundStatement bound = purgeAllBySericvePreparedStmt.bind()
				.setString("service_name", serviceName);
		session.execute(bound);
	}
}
