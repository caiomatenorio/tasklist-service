package io.github.caiomatenorio.tasklist_service.security.token;

import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.Getter;

@Getter
public class AuthenticationToken extends UsernamePasswordAuthenticationToken {

    public AuthenticationToken(String username, UUID sessionId, UUID userId, String name) {
        super(username, null, List.of());
        setDetails(new AuthenticationTokenDetails(sessionId, userId, name));
    }
}
