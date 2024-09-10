package com.loginapi.loginapi.model;

import java.io.Serializable;
import java.util.UUID;

import com.loginapi.loginapi.enums.RoleEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="tbusers")
@Getter
@Setter
public class UserModel implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID id;
    private String email;
    private String name;
    private String password;
    private RoleEnum role;

}
