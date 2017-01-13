package com.ai.message.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.message.service.MessageService;

@Controller("/SOAServlet")
public class MessageServlet {
	@Autowired
	private MessageService service;
	
		static MessageFactory messageFactory = null;

	    static {
	        try {
	            messageFactory = MessageFactory.newInstance();
	        } catch (Exception ex) {
	        	ex.printStackTrace();
	        }
	     };

	@PostMapping     
	public @ResponseBody void process( HttpServletRequest request, HttpServletResponse response) throws IOException, SOAPException{
	      MimeHeaders headers = getHeaders(request);
	      InputStream is = request.getInputStream();
	      SOAPMessage msg = messageFactory.createMessage(headers, is);
	      service.process(headers, msg);
	      response.setStatus(200);
	}

	private MimeHeaders getHeaders(HttpServletRequest request) {
		Enumeration<String> headerNames = request.getHeaderNames();
	    MimeHeaders headers = new MimeHeaders();

	    while (headerNames.hasMoreElements()) {
	        String headerName = (String)headerNames.nextElement();
	        String headerValue = request.getHeader(headerName);

	        StringTokenizer values = new StringTokenizer(headerValue, ",");
	        while (values.hasMoreTokens()) {
	            headers.addHeader(headerName, values.nextToken().trim());
	        }
	    }
	    return headers;
	}
	
}
