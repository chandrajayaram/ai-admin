package com.ai.message.service;

import java.io.IOException;
import java.net.URL;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

public class TestPost {
	public static void main(String[] args) throws SOAPException, IOException {
		MimeHeaders mimeHeaders = new MimeHeaders();
		mimeHeaders.addHeader("Content-Type", "text/xml");
		mimeHeaders.addHeader("SOAPAction", "http://www.caiso.com/soa/receiveAvailabilityResultsCaiso_v1");
		MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
		SOAPMessage  soapMessage = factory.createMessage(mimeHeaders , ClassLoader.getSystemResourceAsStream("data.xml"));		
		SOAPConnectionFactory soapConnectionFactory =
			    SOAPConnectionFactory.newInstance();
		SOAPConnection connection = soapConnectionFactory.createConnection();

		java.net.URL endpoint = new URL("http://localhost:8080/SOAServlet");

		SOAPMessage response = connection.call(soapMessage, endpoint);
		
	}
}
