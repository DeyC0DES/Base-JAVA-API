package com.loginapi.loginapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
    
    private String emailTo;
    private String subject;
    private String text;

}
