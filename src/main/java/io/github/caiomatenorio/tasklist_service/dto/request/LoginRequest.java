package io.github.caiomatenorio.tasklist_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
                @NotBlank String username,
                @NotBlank String password) {
}