package io.github.caiomatenorio.tasklist_service.dto.response;

public record LoginResponse(
                String jwt,
                String refreshToken) {
}
