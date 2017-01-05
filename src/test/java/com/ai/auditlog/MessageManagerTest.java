package com.ai.auditlog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.ai.CassandraConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CassandraConfig.class)
public class MessageManagerTest {
	
	@Autowired
	private MessageManager manager;
	@Autowired
	private MessageFinder finder;

	@Test
	public void testCreate() {
		Date timeStamp = new Date();
		Message message = new Message();
		message.setServiceName("testServiceName");
		message.setId(UUID.randomUUID().toString());
		message.setTimeStamp(timeStamp);
		message.setSoapAction("soapAction");
		manager.create(message);
		List<Message> result = finder.findMessageByDateRange("testServiceName", timeStamp, timeStamp, 0, 0);
		Assert.notNull(result, "Create and find failed");
	}

	@Test
	public void testDelete() {
		Date timeStamp = new Date();
		Message message = new Message();
		message.setServiceName("testServiceName");
		message.setId(UUID.randomUUID().toString());
		message.setTimeStamp(timeStamp);
		message.setSoapAction("soapAction");
		manager.create(message);
		List<Message> result = finder.findMessageByDateRange("testServiceName", timeStamp, timeStamp, 0, 0);
		Assert.notNull(result, "Create and find failed");
		manager.delete("testServiceName", timeStamp);
		List<Message> deleted = finder.findMessageByDateRange("testServiceName", timeStamp, timeStamp, 0, 0);
		Assert.isNull(deleted, "message was not deleted successfully");
	}

	@Test
	public void testPurgeMessages() {
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
		calInstance.add(Calendar.DAY_OF_MONTH, -1);
		ltimeStamp = calInstance.getTime();
		List<Message> messages = finder.findMessageByDateRange("testServiceName", timeStamp, ltimeStamp, 0, 5);
		Assert.isTrue(messages.size() == 5, "unable to save and retrive 5 messages");
		manager.purgeMessages("testServiceName", timeStamp, ltimeStamp);
		messages = finder.findMessageByDateRange("testServiceName", timeStamp, ltimeStamp, 0, 5);
		Assert.isTrue(messages.size() == 0, "unable to delete message for a range");
	}

	@Test
	public void testPurgeAllMessage() {

	}

}
