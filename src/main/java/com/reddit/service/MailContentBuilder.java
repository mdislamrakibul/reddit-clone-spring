package com.reddit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.reddit.model.NotificationEmail;

@Service
public class MailContentBuilder {

	@Autowired
	private TemplateEngine templateEngine;
	
	public String build(String string){
        Context context = new Context() ;
        context.setVariable("message", string);
        return templateEngine.process("mailTemplate", context);
    }
}
