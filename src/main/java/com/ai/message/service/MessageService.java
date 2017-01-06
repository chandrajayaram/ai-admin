package com.ai.message.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.message.domain.Message;

@Service
public class MessageService {

	private MessageFinder messageFinder;
	
	@Autowired
	public void setMessageFinder(MessageFinder messageFinder) {
		this.messageFinder = messageFinder;
	}
	public List<Message> findMessageByDateRange(String serviceName, Date startTimeStamp, Date endTimeStamp, int start,
			int count){
		return messageFinder.findMessageByServiceName(serviceName, start, count);
		//return messageFinder.findMessageByDateRange(serviceName, startTimeStamp, endTimeStamp, start, count);
	}
	
}
