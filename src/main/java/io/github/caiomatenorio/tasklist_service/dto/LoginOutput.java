package io.github.caiomatenorio.tasklist_service.dto;

public record LoginOutput(
        String jwt,
        String refreshToken) {
}
