package com.loginapi.loginapi.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.loginapi.loginapi.dtos.EmailDto;
import com.loginapi.loginapi.model.TicketModel;
import com.loginapi.loginapi.model.UserModel;

@Component
public class RecoverMessage {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${broker.queue.email.name}")
    private String routingKey;

    public void publishMessageEmail(UserModel model, TicketModel ticket) {
        var emailDto = new EmailDto();
        emailDto.setEmailTo(model.getEmail());
        emailDto.setSubject("forget password");
        emailDto.setText(model.getName() + ", You can reset your password with the code below\n" + //
                        "\n" + // 
                        ticket.getCode() + "\n" + //
                        "\n" + //
                        "If you don't remember to ask for a reset code, enter another password\n" + //
                        "\n" + //
                        "*request created in: " + ticket.getCreatedIn() + "*");
        
        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }

}
