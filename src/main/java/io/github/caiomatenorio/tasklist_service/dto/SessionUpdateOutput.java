package io.github.caiomatenorio.tasklist_service.dto;

import java.util.UUID;

public record SessionUpdateOutput(
                UUID id,
                String username) {
}
