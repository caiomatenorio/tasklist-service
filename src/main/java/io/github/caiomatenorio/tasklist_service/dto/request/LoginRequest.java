package io.github.caiomatenorio.tasklist_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Field 'username' must not be blank.") String username,
        @NotBlank(message = "Field 'password' must not be blank.") String password) {
}