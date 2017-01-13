package com.ai.message.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ai.message.domain.Message;
import com.ai.message.service.MessageService;

@Controller
public class MessageController {
	
	private MessageService messageService;
	
	@Autowired
    public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	

	@GetMapping("/messagelist")
    public ModelAndView processSearchQuery(@RequestParam("serviceName") String serviceName, @RequestParam("searchBy") String searchBy, @RequestParam("date") String date, @RequestParam("startDate") String startDate, String endDate) throws ParseException{
		ModelAndView model = new ModelAndView("messagelist");
		if(serviceName == null || serviceName.isEmpty()){
			return model;
		}
		if("serviceName".equals(searchBy)){
			model.addObject("messageList", messageService.findMessageByServiceName(serviceName));
			return model;
		}
		SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
		if("date".equals(searchBy)){
			Date startTimeStamp = fmt.parse(date);
			model.addObject("messageList", messageService.findMessageByDate(serviceName, startTimeStamp , 0, 0));
			return model;
		}
		if("dateRange".equals(searchBy)){
			Date startTimeStamp = fmt.parse(startDate);
			Date endTimeStamp = fmt.parse(endDate);
			model.addObject("messageList", messageService.findMessageByDateRange(serviceName, startTimeStamp, endTimeStamp, 0, 0));
			return model;
		}
		return null;
	}

	private List<Message> getList(String serviceName) {
		return messageService.findMessageByDateRange(serviceName, null, null, 0, 0);
		/*List<Message> messageList = new ArrayList<>();
		for(int i =0;i<10;i++){
			Message m = new Message();
			m.setId(i+"");
			m.setTimeStamp(new Date());
			m.setHttpHeaders(new HashMap<>());
			messageList.add(m);
		}
		return messageList;*/
	}
	
	@GetMapping("/messagedetail")
    public ModelAndView readMessage(@RequestParam("payloadId") String payloadId){
		Message msg = messageService.findMessageById(payloadId);
		ModelAndView model = new ModelAndView("messagedetail");
		model.addObject("message", msg);
		return model;
	}
	
}
