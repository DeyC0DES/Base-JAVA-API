package com.loginapi.loginapi.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loginapi.loginapi.model.UserModel;


public interface UserRepository extends JpaRepository<UserModel, UUID> {
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByName(String name);
}
