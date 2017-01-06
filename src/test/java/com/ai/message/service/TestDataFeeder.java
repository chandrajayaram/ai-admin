package com.ai.message.service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.CassandraConfig;
import com.ai.message.domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CassandraConfig.class)
public class TestDataFeeder {
	
	@Autowired
	private MessageManager manager;
	
	@Test
	public void feedTestData(){
		Calendar calInstance = Calendar.getInstance();
		Date timeStamp = new Date();
		Date ltimeStamp = timeStamp;
		calInstance.setTime(timeStamp);
		for (int i = 1; i <= 5; i++) {
			Message message = new Message();
			message.setServiceName("testServiceName");
			message.setId(UUID.randomUUID().toString());
			message.setTimeStamp(ltimeStamp);
			message.setSoapAction("soapAction");
			manager.create(message);
			calInstance.add(Calendar.DAY_OF_MONTH, 1);
			ltimeStamp = calInstance.getTime();
		}
	}
	
}
