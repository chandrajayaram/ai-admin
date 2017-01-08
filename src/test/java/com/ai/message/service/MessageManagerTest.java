package com.ai.message.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.ai.CassandraConfig;
import com.ai.message.domain.Message;
import com.ai.message.service.MessageFinder;
import com.ai.message.service.MessageManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CassandraConfig.class)
public class MessageManagerTest {
	
	@Autowired
	private MessageManager manager;
	@Autowired
	private MessageFinder finder;
	
	@Before
	public void testClear(){
		manager.purgeAllMessage("testServiceName");
	}
	
	@Test
	public void testCreate() throws SOAPException, IOException {
		Date timeStamp = new Date();
		Message message = new Message();
		String id =UUID.randomUUID().toString();
		message.setServiceName("testServiceName");
		message.setId(id);
		message.setTimeStamp(timeStamp);
		message.setSoapAction("soapAction");
		MimeHeaders mimeHeaders = new MimeHeaders();
		mimeHeaders.addHeader("Content-Type", "application/soap+xml");
		message.setMimeHeaders(mimeHeaders);
		MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		SOAPMessage  soapMessage = factory.createMessage(mimeHeaders , ClassLoader.getSystemResourceAsStream("data.xml"));
		message.setSoapMessage(soapMessage);
		manager.create(message);
		Message result = finder.findMessageById("testServiceName", timeStamp, id);
		Assert.notNull(result.getSoapMessage(), "Create and find failed");
	}

	//@Test
	public void testDelete() throws SOAPException, IOException {
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
		Assert.isTrue(deleted.size() == 0, "message was not deleted successfully");
	}

	//@Test
	public void testPurgeMessages() throws SOAPException, IOException {
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
