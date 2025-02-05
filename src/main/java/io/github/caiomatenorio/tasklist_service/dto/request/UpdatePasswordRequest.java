package io.github.caiomatenorio.tasklist_service.dto.request;

import io.github.caiomatenorio.tasklist_service.validation.annotation.ValidPassword;

public record UpdatePasswordRequest(
        String currentPassword,
        @ValidPassword String newPassword) {
}
