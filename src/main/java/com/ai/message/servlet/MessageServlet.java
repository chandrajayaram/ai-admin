package com.ai.message.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.ai.message.service.MessageService;

public class MessageServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private MessageService service;

	static MessageFactory messageFactory = null;
	
	public MessageServlet(MessageService service){
		this.service = service;
	}
	

	static {
		try {
			messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	};

	  public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext (this);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MimeHeaders headers = getHeaders(req);
		SOAPMessage msg;
		try {
			msg = messageFactory.createMessage(headers, req.getInputStream());
			service.process(headers, msg);
			resp.setStatus(200);
		} catch (SOAPException e) {
			e.printStackTrace();
		}

	}

	private MimeHeaders getHeaders(HttpServletRequest request) {
		Enumeration<String> headerNames = request.getHeaderNames();
		MimeHeaders headers = new MimeHeaders();

		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			String headerValue = request.getHeader(headerName);

			StringTokenizer values = new StringTokenizer(headerValue, ",");
			while (values.hasMoreTokens()) {
				headers.addHeader(headerName, values.nextToken().trim());
			}
		}
		return headers;
	}

}
