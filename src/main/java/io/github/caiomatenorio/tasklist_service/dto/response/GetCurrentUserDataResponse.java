package io.github.caiomatenorio.tasklist_service.dto.response;

import java.util.UUID;

public record GetCurrentUserDataResponse(
        UUID id,
        String username,
        String name) {
}
