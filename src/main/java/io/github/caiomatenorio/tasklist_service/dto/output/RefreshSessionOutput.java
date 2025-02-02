package io.github.caiomatenorio.tasklist_service.dto.output;

import java.util.UUID;

public record RefreshSessionOutput(
                UUID id,
                String username) {
}
