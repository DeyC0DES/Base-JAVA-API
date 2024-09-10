package com.loginapi.loginapi.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loginapi.loginapi.enums.TicketEnum;
import com.loginapi.loginapi.model.TicketModel;


public interface TicketRepository extends JpaRepository<TicketModel, UUID> {
    Optional<TicketModel> findByUserId(UUID id);
    Optional<TicketModel> findByUserEmail(String email);
    Optional<TicketModel> findByCode(String code);
    List<TicketModel> findByTicketStats(TicketEnum ticketStats);
}
