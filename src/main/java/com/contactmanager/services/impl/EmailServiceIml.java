package com.contactmanager.services.impl;

import com.contactmanager.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceIml implements EmailService {


    @Autowired(required = false)
    private JavaMailSender emailSender;

    @Value("${spring.mail.properties.domain_name:localhost}")
    private String domainName;

    @Override
    public void sendEmail(String to, String subject, String body) {
        if (emailSender == null) {
            return; // Skip email sending if email service is not configured
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(domainName);

        emailSender.send(message);

    }

    @Override
    public void sendEmailWithHtml() {

    }

    @Override
    public void sendEmailWithAttachment() {

    }
}
