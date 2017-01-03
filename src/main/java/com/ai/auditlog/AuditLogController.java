package com.ai.auditlog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuditLogController {
    @PostMapping("/auditlog")
    public ModelAndView processSearchQuery(@RequestParam("serviceName") String serviceName){
		ModelAndView model = new ModelAndView("auditlog");
		model.addObject("messageList", getList());
		return model;
	}

	private List<Message> getList() {
		List<Message> messageList = new ArrayList<>();
		for(int i =0;i<10;i++){
			Message m = new Message();
			m.setId(i+"");
			m.setTimeStamp(new Date());
			m.setHttpHeaders(new HashMap<>());
			messageList.add(m);
		}
		return messageList;
	}
}
