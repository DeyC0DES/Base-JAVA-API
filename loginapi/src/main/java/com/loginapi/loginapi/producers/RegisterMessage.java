package com.loginapi.loginapi.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.loginapi.loginapi.dtos.EmailDto;
import com.loginapi.loginapi.model.UserModel;

@Component
public class RegisterMessage {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Value("${broker.queue.email.name}")
    private String routingKey;

    public void publishMessageEmail(UserModel model) {
        var emailDto = new EmailDto();
        emailDto.setEmailTo(model.getEmail());
        emailDto.setSubject("Thanks to test that API :)");
        emailDto.setText(model.getName() + ", Welcome to a base API u can change anything u want!!! :D\n" + //
                        "\n" + //
                        "*dont forget to mention me Dey.codes*");
        
        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }

}
