package io.github.caiomatenorio.tasklist_service.dto.request;

import io.github.caiomatenorio.tasklist_service.validation.annotation.ValidUsername;

public record UpdateUsernameRequest(
        @ValidUsername String username,
        String password) {
}
