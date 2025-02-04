package io.github.caiomatenorio.tasklist_service.controller;

import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpHeaders;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalCookie;
import io.github.caiomatenorio.tasklist_service.convention.ConventionalResponseBody;
import io.github.caiomatenorio.tasklist_service.dto.request.LoginRequest;
import io.github.caiomatenorio.tasklist_service.service.SessionService;
import io.github.caiomatenorio.tasklist_service.service.UserService;
import io.github.caiomatenorio.tasklist_service.util.CookieUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final SessionService sessionService;
    private final CookieUtil cookieUtil;

    @PostMapping
    public ResponseEntity<ConventionalResponseBody> login(@Valid @RequestBody LoginRequest request) {
        UUID sessionId = userService.login(request);

        Set<ConventionalCookie> sessionCookies = sessionService.createSessionCookies(sessionId);
        HttpHeaders headers = cookieUtil.toHttpHeaders(sessionCookies);

        return new ConventionalResponseBody.Success<>(200, "Login successful").toResponseEntity(headers);
    }
}
