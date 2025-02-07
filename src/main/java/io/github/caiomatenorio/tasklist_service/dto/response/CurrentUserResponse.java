package io.github.caiomatenorio.tasklist_service.dto.response;

import java.util.UUID;

public record CurrentUserResponse(
                UUID id,
                String username,
                String name) {
}
