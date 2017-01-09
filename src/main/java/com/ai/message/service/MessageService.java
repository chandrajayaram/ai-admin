package com.ai.message.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.soap.MimeHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.message.domain.Message;

@Service
public class MessageService {

	private MessageFinder messageFinder;
	private MessageManager messageManager;

	@Autowired
	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}
	@Autowired
	public void setMessageFinder(MessageFinder messageFinder) {
		this.messageFinder = messageFinder;
	}
	
	public List<Message> findMessageByServiceName(String serviceName){
		
		return messageFinder.findMessageByServiceName(serviceName, 0, 0);
	}
	
	public List<Message> findMessageByDateRange(String serviceName, Date startTimeStamp, Date endTimeStamp, int start,
			int count){
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTimeStamp);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		startTimeStamp= cal.getTime();
		
		cal.setTime(endTimeStamp);
		cal.set(Calendar.HOUR_OF_DAY, 11);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		
		endTimeStamp = cal.getTime();
		
		return messageFinder.findMessageByDateRange(serviceName, startTimeStamp, endTimeStamp, 5, 0);
	}
	
	
	public List<Message> findMessageByDate(String serviceName, Date startTimeStamp, int start,
			int count){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTimeStamp);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		startTimeStamp= cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date endTimeStamp = cal.getTime();
		
		return messageFinder.findMessageByDateRange(serviceName, startTimeStamp, endTimeStamp, 5, 0);
	}
	
	
	public Message findMessageById(String payloadId){
		return messageFinder.findMessageById(payloadId);
	}
	
	public void process(MimeHeaders headers, String soapMessage) {

		
	}
	
}
