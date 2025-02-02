package io.github.caiomatenorio.tasklist_service.dto.request;

import io.github.caiomatenorio.tasklist_service.validation.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
                @NotBlank @Size(max = 255) String name,
                @NotBlank @Size(max = 255) String username,
                @ValidPassword String password) {
}
