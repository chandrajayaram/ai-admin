/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ai.message.service;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ai.message.domain.Message;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

/**
 *
 * @author Chandra
 */
@Repository
public class MessageManager {

	private PreparedStatement insertPreparedStmt;
	private PreparedStatement deletePreparedStmt;
	private PreparedStatement purgePreparedStmt;
	private PreparedStatement purgeAllPreparedStmt;
	private PreparedStatement purgeAllBySericvePreparedStmt; 
	
	private static final String INSERT_MESSAGE="INSERT INTO message(service_name, create_ts, payload_id , soap_action) values (:service_name, :create_ts, :payload_id , :soap_action)";
	private static final String DELETE_MESSAGE="DELETE FROM message WHERE service_name = :service_name and create_ts = :create_ts";
	private static final String PURGE_MESSAGE="DELETE FROM message WHERE service_name = :service_name and create_ts >= :start_ts and create_ts <= : end_ts";
	private static final String PURGE_MESSAGE_BY_SERVICE="DELETE FROM message WHERE service_name = :service_name";
	
	private static final String PURGE_ALL_MESSAGES="";
	
	private Session session;
	
	@Autowired
	public void setSession(Session session) {
		this.session = session;
	}

	@PostConstruct
	public void init(){
		insertPreparedStmt = session.prepare(INSERT_MESSAGE);
		deletePreparedStmt = session.prepare(DELETE_MESSAGE);
		purgePreparedStmt = session.prepare(PURGE_MESSAGE);
		purgeAllBySericvePreparedStmt = session.prepare(PURGE_MESSAGE_BY_SERVICE); 
		//purgeAllPreparedStmt = session.prepare(PURGE_MESSAGE);
		
	}
	
	public void create(Message message) {
		BoundStatement bound = insertPreparedStmt.bind()
				.setString("service_name", message.getServiceName())
				.setTimestamp("create_ts", message.getTimeStamp())
				.setString("payload_id", message.getId())
				.setString("soap_action", message.getSoapAction());
		session.execute(bound);
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
