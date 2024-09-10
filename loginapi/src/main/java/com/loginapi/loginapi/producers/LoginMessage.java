package com.loginapi.loginapi.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.loginapi.loginapi.dtos.EmailDto;
import com.loginapi.loginapi.model.UserModel;

@Component
public class LoginMessage {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${broker.queue.email.name}")
    private String routingKey;

    public void publishMessageEmail(UserModel model) {
        var emailDto = new EmailDto();
        emailDto.setEmailTo(model.getEmail());
        emailDto.setSubject("New login detected :o");
        emailDto.setText(model.getName() + ", If you don't remember logging in, don't worry, this API has no information\n" + //
                        "\n" + //
                        "*unless you've started storing information*");
        
        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }

}
