package com.loginapi.loginapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.loginapi.loginapi.model.UserModel;
import com.loginapi.loginapi.producers.LoginMessage;
import com.loginapi.loginapi.producers.RegisterMessage;
import com.loginapi.loginapi.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository repository;
    private final LoginMessage loginMessage;
    private final RegisterMessage registerMessage;

    public UserModel save(UserModel model) {
        this.repository.save(model);
        return model;
    }

    public void loginMessageEmail(UserModel model) {
        this.loginMessage.publishMessageEmail(model);
    }

    public void registerMessageEmail(UserModel model) {
        this.registerMessage.publishMessageEmail(model);
    }

    public List<UserModel> findAll() {
        return this.repository.findAll();
    }

    public Optional<UserModel> findByEmail(String email) {
        return this.repository.findByEmail(email);
    }

    public Optional<UserModel> findByName(String name) {
        return this.repository.findByName(name);
    }

    public void delete(UserModel model) {
        this.repository.delete(model);
    }

}
