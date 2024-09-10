package com.loginapi.loginapi.dtos;

import com.loginapi.loginapi.enums.RoleEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateRecordDto(@NotNull @NotEmpty @Email String email,
                              @NotNull @NotEmpty String name,
                              @NotNull @NotEmpty String password,
                              @NotNull RoleEnum role) {
    
}
