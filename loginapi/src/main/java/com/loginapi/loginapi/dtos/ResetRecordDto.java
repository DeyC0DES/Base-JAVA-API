package com.loginapi.loginapi.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ResetRecordDto(@NotNull @NotEmpty String code,
                             @NotNull @NotEmpty String password,
                             @NotNull @NotEmpty String confirm) {
    
}
