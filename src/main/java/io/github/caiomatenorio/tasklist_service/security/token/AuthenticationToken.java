package io.github.caiomatenorio.tasklist_service.security.token;

import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.Getter;

@Getter
public class AuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final UUID userId;
    private final UUID sessionId;
    private final AuthenticationTokenDetails details;

    public AuthenticationToken(UUID userId, UUID sessionId, String username, String name) {
        super(userId, sessionId, null);

        this.userId = userId;
        this.sessionId = sessionId;
        this.details = new AuthenticationTokenDetails(username, name);

        setAuthenticated(true);
        setDetails(details);
    }
}
