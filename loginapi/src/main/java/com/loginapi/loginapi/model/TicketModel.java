package com.loginapi.loginapi.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.loginapi.loginapi.enums.TicketEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="tbtickets")
@Getter
@Setter
public class TicketModel implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID id;
    private UUID userId;
    private String userEmail;
    private String code;
    private String description;
    private TicketEnum ticketStats;
    private LocalDateTime createdIn;

}
