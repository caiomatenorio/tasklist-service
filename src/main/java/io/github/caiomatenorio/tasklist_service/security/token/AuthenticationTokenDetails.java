package io.github.caiomatenorio.tasklist_service.security.token;

public record AuthenticationTokenDetails(
        String username,
        String name) {
}
