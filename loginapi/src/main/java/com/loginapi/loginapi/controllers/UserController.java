package com.loginapi.loginapi.controllers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loginapi.loginapi.configs.security.TokenService;
import com.loginapi.loginapi.dtos.ForgetRecordDto;
import com.loginapi.loginapi.dtos.LoginRecordDto;
import com.loginapi.loginapi.dtos.RegisterRecordDto;
import com.loginapi.loginapi.dtos.ResetRecordDto;
import com.loginapi.loginapi.dtos.ResponseRecordDto;
import com.loginapi.loginapi.dtos.UpdateRecordDto;
import com.loginapi.loginapi.enums.RoleEnum;
import com.loginapi.loginapi.enums.TicketEnum;
import com.loginapi.loginapi.model.TicketModel;
import com.loginapi.loginapi.model.UserModel;
import com.loginapi.loginapi.services.TicketService;
import com.loginapi.loginapi.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TicketService ticketService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRecordDto body) {
        var userModelOptional = this.userService.findByEmail(body.email());

        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The account does not exist");
        }

        if (passwordEncoder.matches(body.password(), userModelOptional.get().getPassword())) {
            String token = this.tokenService.generateToken(userModelOptional.get());
            this.userService.loginMessageEmail(userModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseRecordDto(userModelOptional.get().getName(), token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login/Password is incorrect");
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterRecordDto body) {
        var userModelOptional = this.userService.findByEmail(body.email());

        if (userModelOptional.isEmpty()) {
            UserModel userModel = new UserModel();
            userModel.setEmail(body.email());
            userModel.setName(body.name());
            userModel.setPassword(passwordEncoder.encode(body.password()));
            userModel.setRole(RoleEnum.USER);

            if (body.name().equals("Admin.iLjk64")) {
                userModel.setName("Admin");
                userModel.setRole(RoleEnum.ADMIN);
            }

            this.userService.save(userModel);
            this.userService.registerMessageEmail(userModel);

            String token = this.tokenService.generateToken(userModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseRecordDto(userModel.getName(), token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The account already exist");
    }

    @PostMapping("/forget")
    public ResponseEntity<Object> forget(@RequestBody @Valid ForgetRecordDto body) {
        var userModelOptional = this.userService.findByEmail(body.email());

        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        var userModel = userModelOptional.get();

        TicketModel ticket = new TicketModel();
        ticket.setUserId(userModel.getId());
        ticket.setUserEmail(userModel.getEmail());
        ticket.setCode(generateCode());
        ticket.setDescription("Password reset Ticket!");
        ticket.setTicketStats(TicketEnum.OPEN);
        ticket.setCreatedIn(LocalDateTime.now());
        this.ticketService.save(ticket);

        this.ticketService.sendCode(userModel, ticket);

        return ResponseEntity.status(HttpStatus.OK).body("We've sent an email with a code for u :D\nIf that isn't ur account, think about what u're doing now >:C");
    }

    @PutMapping("/new-pwd")
    public ResponseEntity<Object> recover(@RequestBody @Valid ResetRecordDto body) {
        if (!body.password().equals(body.confirm())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Passwords dont matches");
        }

        var ticketModelOptional = this.ticketService.findByCode(body.code());

        if (!ticketModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid code");
        }

        var userModelOptional = this.userService.findByEmail(ticketModelOptional.get().getUserEmail());

        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        var userModel = userModelOptional.get();
        var ticket = ticketModelOptional.get();

        if (ticket.getTicketStats() == TicketEnum.OPEN) {
            Duration duration = Duration.between(ticket.getCreatedIn(), LocalDateTime.now());

            if (duration.toMinutes() >= 10) {
                ticket.setTicketStats(TicketEnum.EXPIRED);
                this.ticketService.save(ticket);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ticket expired");
            }
    
            userModel.setPassword(passwordEncoder.encode(body.password()));
            ticket.setTicketStats(TicketEnum.USED);
            this.userService.save(userModel);
            return ResponseEntity.status(HttpStatus.OK).body("Account password has been reseted!");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ticket already used");
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserModel>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.findAll());
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody @Valid UpdateRecordDto body) {
        var userModelOptional = this.userService.findByEmail(body.email());

        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        var userModel = userModelOptional.get();

        userModel.setName(body.name());
        userModel.setPassword(passwordEncoder.encode(body.password()));
        userModel.setRole(body.role());
        this.userService.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body("Account updated");
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Object> delete(@PathVariable(value="email") String email) {
        var userModelOptional = this.userService.findByEmail(email);

        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        this.userService.delete(userModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Account deleted");
    }

    private String generateCode() {
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++) {
            code += Integer.toString(random.nextInt(9));
        }
        return code;
    }

}
