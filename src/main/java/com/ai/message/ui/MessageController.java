package com.ai.message.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	

	@PostMapping("/messagelist")
    public ModelAndView processSearchQuery(@RequestParam("serviceName") String serviceName){
		ModelAndView model = new ModelAndView("messagelist");
		model.addObject("messageList", getList(serviceName));
		return model;
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
