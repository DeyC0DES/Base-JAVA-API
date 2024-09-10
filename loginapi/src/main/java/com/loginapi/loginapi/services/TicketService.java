package com.loginapi.loginapi.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loginapi.loginapi.enums.TicketEnum;
import com.loginapi.loginapi.model.TicketModel;
import com.loginapi.loginapi.model.UserModel;
import com.loginapi.loginapi.producers.RecoverMessage;
import com.loginapi.loginapi.repositories.TicketRepository;

@Service
public class TicketService {
    
    @Autowired
    private TicketRepository repository;

    @Autowired
    private RecoverMessage message;

    public TicketModel save(TicketModel model) {
        this.repository.save(model);
        return model;
    }

    public List<TicketModel> findAll() {
        return this.repository.findAll();
    }

    public void sendCode(UserModel model, TicketModel tikcet) {
        this.message.publishMessageEmail(model, tikcet);
    }

    public List<TicketModel> findByStats(TicketEnum stats) {
        return this.repository.findByTicketStats(stats);
    }

    public Optional<TicketModel> findByUserId(UUID id) {
        return this.repository.findByUserId(id);
    }

    public Optional<TicketModel> findByUserEmail(String email) {
        return this.repository.findByUserEmail(email);
    }

    public Optional<TicketModel> findByCode(String code) {
        return this.repository.findByCode(code);
    }

    public void delete(TicketModel model) {
        this.repository.delete(model);
    }

}
