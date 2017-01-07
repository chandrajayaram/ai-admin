package com.ai.message.ui;

import static java.nio.file.Files.readAllBytes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SoapMessageController {

	@GetMapping("/soapmessage")
	public ModelAndView createSoapMessage() throws URISyntaxException, IOException{
		 Path p = Paths.get(ClassLoader.getSystemResource("data.xml").toURI());
		 ModelAndView model = new ModelAndView("soapmessage");
		 model.addObject("message", new String(readAllBytes(p)));
		 return model;
	}
}
