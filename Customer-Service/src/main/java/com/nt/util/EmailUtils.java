package com.nt.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {
	@Autowired
	private JavaMailSender mail;
	
	public boolean sendEmailMessage(String toMail,String subject,String body) throws Exception{
		boolean mailSent = false;
		try {
			MimeMessage msg = mail.createMimeMessage();
			MimeMessageHelper help = new MimeMessageHelper(msg,true);
			help.setTo(toMail);
			help.setSentDate(new Date());
			help.setSubject(subject);
			help.setText(body,true);
			mail.send(msg);
			mailSent=true;
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return mailSent;
	}
}
