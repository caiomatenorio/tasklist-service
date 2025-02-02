package io.github.caiomatenorio.tasklist_service.dto.output;

public record LoginOutput(
                String jwt,
                String refreshToken) {
}
