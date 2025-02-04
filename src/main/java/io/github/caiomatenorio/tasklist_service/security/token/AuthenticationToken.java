package io.github.caiomatenorio.tasklist_service.security.token;

import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.Getter;

@Getter
public class AuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String username;
    private final UUID sessionId;
    private final String name;

    public AuthenticationToken(String username, UUID sessionId, String name) {
        super(username, sessionId, null);
        super.setDetails(name);

        this.username = username;
        this.sessionId = sessionId;
        this.name = name;
    }
}
