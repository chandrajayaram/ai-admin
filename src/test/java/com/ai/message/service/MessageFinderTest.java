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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.ai.CassandraConfig;
import com.ai.message.domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CassandraConfig.class)
public class MessageFinderTest {
	@Autowired
	private MessageManager manager;
	@Autowired
	private MessageFinder finder;
	private String serviceName = "testServiceName";
	
	@Before
	public void setup() throws SOAPException, IOException{
		Calendar calInstance = Calendar.getInstance();
		Date timeStamp = new Date();
		Date ltimeStamp = timeStamp;
		calInstance.setTime(timeStamp);
		for (int i = 1; i <= 5; i++) {
			Message message = new Message();
			String id =UUID.randomUUID().toString();
			message.setServiceName(serviceName);
			message.setId(id);
			message.setTimeStamp(ltimeStamp);
			message.setSoapAction("soapAction");
			MimeHeaders mimeHeaders = new MimeHeaders();
			mimeHeaders.addHeader("Content-Type", "application/soap+xml");
			message.setMimeHeaders(mimeHeaders);
			MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
			SOAPMessage  soapMessage = factory.createMessage(mimeHeaders , ClassLoader.getSystemResourceAsStream("data.xml"));
			message.setSoapMessage(soapMessage);
			manager.create(message);
			calInstance.add(Calendar.DAY_OF_MONTH, 1);
			ltimeStamp = calInstance.getTime();
		}	
	}
	
	@After
	public void teardown(){
		manager.purgeAllMessage("testServiceName");
	}
	
	@Test
	public void testFindMessageByServiceName() throws SOAPException, IOException {
		List<Message> messages = finder.findMessageByServiceName(serviceName, 5, 0);
		Assert.isTrue(messages.size() == 5, "unable to save and retrive 5 messages");
	}

	@Test
	public void testFindMessageByDate() {
		Date startTimeStamp = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTimeStamp);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		startTimeStamp= cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date endTimeStamp = cal.getTime();
		
		List<Message> messages = finder.findMessageByDateRange(serviceName, startTimeStamp, endTimeStamp, 5, 0);
		Assert.isTrue(messages.size() == 1, "unable to retrieve message by date");

	}

	@Test
	public void testFindMessageByDateRange() {
		Date startTimeStamp = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTimeStamp);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		startTimeStamp= cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, 5);
		Date endTimeStamp = cal.getTime();
		List<Message> messages = finder.findMessageByDateRange(serviceName, startTimeStamp, endTimeStamp, 5, 0);
		Assert.isTrue(messages.size() == 5, "unable to save and retrive 5 messages");
		
		
	}

}
