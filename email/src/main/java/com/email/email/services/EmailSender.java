package com.email.email.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.email.email.models.EmailModel;

@Service
public class EmailSender {
    
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public void sendEmail(EmailModel model) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(model.getEmailTo());
            message.setSubject(model.getSubject());
            message.setText(model.getText());
            mailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException("Error while sending message: EMAIL SENDER: " + e.getMessage(), e);
        }
    }

}
