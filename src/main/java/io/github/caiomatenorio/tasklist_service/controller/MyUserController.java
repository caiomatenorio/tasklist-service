package io.github.caiomatenorio.tasklist_service.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalCookie;
import io.github.caiomatenorio.tasklist_service.convention.ConventionalResponseBody;
import io.github.caiomatenorio.tasklist_service.dto.request.UpdateNameRequest;
import io.github.caiomatenorio.tasklist_service.dto.request.UpdatePasswordRequest;
import io.github.caiomatenorio.tasklist_service.dto.request.UpdateUsernameRequest;
import io.github.caiomatenorio.tasklist_service.exception.InvalidPasswordException;
import io.github.caiomatenorio.tasklist_service.exception.UnauthorizedException;
import io.github.caiomatenorio.tasklist_service.exception.UsernameAlreadyInUseException;
import io.github.caiomatenorio.tasklist_service.service.UserService;
import io.github.caiomatenorio.tasklist_service.util.CookieUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class MyUserController {
    private final UserService userService;
    private final CookieUtil cookieUtil;

    @GetMapping
    public ResponseEntity<ConventionalResponseBody> getCurrentUserData() throws UnauthorizedException {
        var response = userService.getCurrentUserData();

        return new ConventionalResponseBody.Success<>(200, response).toResponseEntity();
    }

    @PatchMapping("/name")
    public ResponseEntity<ConventionalResponseBody> updateCurrentName(@Valid @RequestBody UpdateNameRequest request)
            throws UnauthorizedException {
        Set<ConventionalCookie> updatedSessionCookies = userService.updateCurrentName(request);
        var headers = cookieUtil.toHttpHeaders(updatedSessionCookies);

        return new ConventionalResponseBody.Success<>(204).toResponseEntity(headers);
    }

    @PatchMapping("/username")
    public ResponseEntity<ConventionalResponseBody> updateCurrentUsername(
            @Valid @RequestBody UpdateUsernameRequest request)
            throws UnauthorizedException, InvalidPasswordException, UsernameAlreadyInUseException {
        Set<ConventionalCookie> updatedSessionCookies = userService.updateCurrentUsername(request);
        var headers = cookieUtil.toHttpHeaders(updatedSessionCookies);

        return new ConventionalResponseBody.Success<>(204).toResponseEntity(headers);
    }

    @PatchMapping("/password")
    public ResponseEntity<ConventionalResponseBody> updateCurrentPassword(
            @Valid @RequestBody UpdatePasswordRequest request) throws UnauthorizedException {
        userService.updateCurrentPassword(request);

        return new ConventionalResponseBody.Success<>(204).toResponseEntity();
    }

    @DeleteMapping
    public ResponseEntity<ConventionalResponseBody> deleteCurrentUser() {
        Set<ConventionalCookie> deletedCookies = userService.deleteCurrentUser();
        var headers = cookieUtil.toHttpHeaders(deletedCookies);

        return new ConventionalResponseBody.Success<>(204).toResponseEntity(headers);
    }
}
