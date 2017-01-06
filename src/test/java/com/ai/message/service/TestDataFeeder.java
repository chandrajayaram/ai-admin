package com.ai.message.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;

public class TestDataFeeder {

	public static Session session() {
		Cluster.Builder builder = Cluster.builder();
		builder.withClusterName("localCluster").addContactPoint("127.0.0.1");
		builder.withLoadBalancingPolicy(new TokenAwarePolicy(new RoundRobinPolicy()));
		Cluster cluster = builder.build();
		return cluster.connect("message_store_ks");
	}

	public static void main(String[] args) {
		Session session =session();
		String INSERT_MESSAGE="INSERT INTO message(service_name, create_ts, payload_id, soap_action,mime_headers) values (:service_name, :create_ts, :payload_id , :soap_action,:mime_headers)";
		PreparedStatement insertPreparedStmt = session.prepare(INSERT_MESSAGE);
		
		Calendar calInstance = Calendar.getInstance();
		Date timeStamp = new Date();
		Date ltimeStamp = timeStamp;
		calInstance.setTime(timeStamp);
		Map<String, String> data = new HashMap<>();
		data.put("Content-type", "application/xml");
		data.put("Content-length", "50");
		for (int i = 1; i <= 5; i++) {
			BoundStatement bound = insertPreparedStmt.bind()
					.setString("service_name", "testServiceName")
					.setTimestamp("create_ts", ltimeStamp)
					.setString("payload_id", UUID.randomUUID().toString())
					.setString("soap_action", "soapAction")
					.setMap("mime_headers", data);
			session.execute(bound);
			calInstance.add(Calendar.DAY_OF_MONTH, 1);
			ltimeStamp = calInstance.getTime();
		}
		
	}

}
