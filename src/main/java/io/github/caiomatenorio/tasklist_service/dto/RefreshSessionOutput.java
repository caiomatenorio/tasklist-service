package io.github.caiomatenorio.tasklist_service.dto;

import java.util.UUID;

public record RefreshSessionOutput(
        UUID id,
        String username) {
}
