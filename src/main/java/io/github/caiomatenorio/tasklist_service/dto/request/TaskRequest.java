package io.github.caiomatenorio.tasklist_service.dto.request;

import io.github.caiomatenorio.tasklist_service.validation.annotation.ValidStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest(
        @NotBlank(message = "Field 'title' must not be blank.") @Size(max = 255, message = "Field 'title' must not be larger than 255 characters.") String title,
        @Size(max = 255, message = "Field 'description' must not be larger than 255 characters.") String description,
        @ValidStatus String status) {
}
