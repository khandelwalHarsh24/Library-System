package com.librarysystem.notify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.librarysystem.common.dto.NotifyRequest;


@Service
public class NotifyServiceImpl implements NotifyService {

	
	@Autowired
    private JavaMailSender mailSender;
	
	@Override
	public void sendNotification(NotifyRequest request) {
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("khandelwalharsh0003@gmail.com");
        message.setSubject("hello");
        message.setText("testing Mail");
        mailSender.send(message);
	}

}
