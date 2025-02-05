package io.github.caiomatenorio.tasklist_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateNameRequest(
        @NotBlank @Size(max = 255) String name) {
}
