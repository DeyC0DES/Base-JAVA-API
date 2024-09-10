package com.email.email.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailModel {
    
    private String emailTo;
    private String subject;
    private String text;

}
