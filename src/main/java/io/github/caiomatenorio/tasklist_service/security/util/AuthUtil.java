package io.github.caiomatenorio.tasklist_service.security.util;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.github.caiomatenorio.tasklist_service.exception.UnauthenticatedException;

@Component
public class AuthUtil {
    public String getCurrentUsername() throws UnauthenticatedException, IllegalArgumentException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            throw new UnauthenticatedException();

        if (!(authentication.getPrincipal() instanceof String))
            throw new IllegalArgumentException("Principal is not a string");

        return (String) authentication.getPrincipal();
    }

    public UUID getCurrentSessionId() throws UnauthenticatedException, IllegalArgumentException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            throw new UnauthenticatedException();

        if (!(authentication.getCredentials() instanceof UUID))
            throw new IllegalArgumentException("Credentials is not a UUID");

        return (UUID) authentication.getCredentials();
    }
}
