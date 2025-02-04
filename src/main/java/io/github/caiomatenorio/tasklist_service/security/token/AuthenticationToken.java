package io.github.caiomatenorio.tasklist_service.security.token;

import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.Getter;

@Getter
public class AuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String username;
    private final UUID sessionId;

    public AuthenticationToken(String username, UUID sessionId) {
        super(username, sessionId, null);
        this.username = username;
        this.sessionId = sessionId;
    }
}
