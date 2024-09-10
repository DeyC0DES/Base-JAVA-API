package com.email.email.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.email.email.dtos.EmailRecordDto;
import com.email.email.models.EmailModel;
import com.email.email.services.EmailSender;

@Component
public class EmailConsumer {
    
    @Autowired
    private EmailSender emailSender;

    @RabbitListener(queues="${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailRecordDto emailRecordDto) {
        var emailModel = new EmailModel();
        BeanUtils.copyProperties(emailRecordDto, emailModel);
        emailSender.sendEmail(emailModel);
    }

}
