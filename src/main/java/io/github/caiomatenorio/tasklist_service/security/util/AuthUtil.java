package io.github.caiomatenorio.tasklist_service.security.util;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.github.caiomatenorio.tasklist_service.exception.UnauthenticatedException;
import io.github.caiomatenorio.tasklist_service.security.token.AuthenticationTokenDetails;

@Component
public class AuthUtil {
    public UUID getCurrentUserId() throws UnauthenticatedException, IllegalArgumentException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            throw new UnauthenticatedException();

        if (!(authentication.getPrincipal() instanceof UUID))
            throw new IllegalArgumentException("Principal is not a UUID");

        return (UUID) authentication.getPrincipal();
    }

    public UUID getCurrentSessionId() throws UnauthenticatedException, IllegalArgumentException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            throw new UnauthenticatedException();

        if (!(authentication.getCredentials() instanceof UUID))
            throw new IllegalArgumentException("Credentials is not a UUID");

        return (UUID) authentication.getCredentials();
    }

    public String getCurrentUsername() throws UnauthenticatedException, IllegalArgumentException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            throw new UnauthenticatedException();

        if (!(authentication.getDetails() instanceof AuthenticationTokenDetails))
            throw new IllegalArgumentException("Details is not an AuthenticationTokenDetails");

        return ((AuthenticationTokenDetails) authentication.getDetails()).username();
    }

    public String getCurrentName() throws UnauthenticatedException, IllegalArgumentException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            throw new UnauthenticatedException();

        if (!(authentication.getDetails() instanceof AuthenticationTokenDetails))
            throw new IllegalArgumentException("Details is not an AuthenticationTokenDetails");

        return ((AuthenticationTokenDetails) authentication.getDetails()).name();
    }
}
