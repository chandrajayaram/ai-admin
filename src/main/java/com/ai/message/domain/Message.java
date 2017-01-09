/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ai.message.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

/**
 *
 * @author Chandra
 */
public class Message {
    private String id;
    private Date timeStamp;
    private String serviceName; 
    private String soapAction;
    private MimeHeaders mimeHeaders;
    private SOAPMessage soapMessage;
    private String status;
    private static MessageFactory factory;
    static{
    	try{
    		factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);	
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    	 
    }
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the timeStamp
     */
    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

   /**
     * @return the httpHeaders
     */
    public MimeHeaders getMimeHeaders() {
        return mimeHeaders;
    }

    /**
     * @param httpHeaders the httpHeaders to set
     */
    public void setMimeHeaders(MimeHeaders mimeHeaders) {
        this.mimeHeaders = mimeHeaders;
    }

    /**
     * @return the message
     */
    public SOAPMessage getSoapMessage() {
        return soapMessage;
    }

    /**
     * @param message the message to set
     */
    public void setSoapMessage(SOAPMessage message) {
        this.soapMessage = message;
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName the serviceName to set
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

	/**
	 * @return the soapAction
	 */
	public String getSoapAction() {
		return soapAction;
	}

	/**
	 * @param soapAction the soapAction to set
	 */
	public void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}
    
	public void setMessage(byte[] message){
		try{
			soapMessage = factory.createMessage(mimeHeaders, new ByteArrayInputStream(message));	
		}catch ( IOException | SOAPException e) {
			e.printStackTrace();
		}
		
    }

	public String getSoapMessageAsString() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			soapMessage.writeTo(out);
			return out.toString();
		} catch (SOAPException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
    
 }
