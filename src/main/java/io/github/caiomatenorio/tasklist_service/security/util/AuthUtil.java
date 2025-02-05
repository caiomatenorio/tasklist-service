package io.github.caiomatenorio.tasklist_service.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.github.caiomatenorio.tasklist_service.exception.UnauthorizedException;
import io.github.caiomatenorio.tasklist_service.security.token.AuthenticationTokenDetails;

@Component
public class AuthUtil {
    public String getCurrentUsername() throws UnauthorizedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if (principal != null && principal instanceof String)
            return (String) principal;

        throw new UnauthorizedException();
    }

    public AuthenticationTokenDetails getCurrentUserDetails() throws UnauthorizedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object details = authentication.getDetails();

        if (details != null && details instanceof AuthenticationTokenDetails)
            return (AuthenticationTokenDetails) details;

        throw new UnauthorizedException();
    }
}
