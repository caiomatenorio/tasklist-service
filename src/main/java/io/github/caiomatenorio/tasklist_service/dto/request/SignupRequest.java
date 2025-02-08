package io.github.caiomatenorio.tasklist_service.dto.request;

import io.github.caiomatenorio.tasklist_service.validation.annotation.ValidPassword;
import io.github.caiomatenorio.tasklist_service.validation.annotation.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank(message = "Field 'name' must not be blank.") @Size(max = 255, message = "Field 'name' must not be larger than 255 characters.") String name,
        @ValidUsername String username,
        @ValidPassword String password) {
}
