package io.github.caiomatenorio.tasklist_service.dto.request;

import io.github.caiomatenorio.tasklist_service.validation.annotation.ValidStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest(
        @NotBlank @Size(max = 255) String title,
        @Size(max = 255) String description,
        @ValidStatus String status) {
}
