package io.github.caiomatenorio.tasklist_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateNameRequest(
        @NotBlank(message = "Field 'name' must not be blank.") @Size(max = 255, message = "Field 'name' must not be larger than 255 characters.") String name) {
}
