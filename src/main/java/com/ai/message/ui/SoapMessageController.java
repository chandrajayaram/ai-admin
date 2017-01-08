package com.ai.message.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.activation.DataHandler;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SoapMessageController {

	@GetMapping("/soapmessage")
	public ModelAndView createSoapMessage() throws URISyntaxException, IOException, SOAPException{
		 MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		 MimeHeaders headers = new MimeHeaders();
		 SOAPMessage  message = factory.createMessage(headers , ClassLoader.getSystemResourceAsStream("data.xml"));
		 
		/* AttachmentPart attachment = message.createAttachmentPart();

		 
		 String stringContent = "Update address for Sunny Skies " +
				    "Inc., to 10 Upbeat Street, Pleasant Grove, CA 95439";

				attachment.setContent(stringContent, "text/plain");
				attachment.setContentId("update_address");

				message.addAttachmentPart(attachment);
*/
		 /*Path p = Paths.get(ClassLoader.getSystemResource("data.xml").toURI());
		 */
		 ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		 message.writeTo(oStream);
		 ModelAndView model = new ModelAndView("soapmessage");
		 System.out.println(oStream.toString());
		 model.addObject("message", oStream.toString());
		 return model;
	}
}
