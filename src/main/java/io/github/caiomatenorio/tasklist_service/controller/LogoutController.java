package io.github.caiomatenorio.tasklist_service.controller;

import org.springframework.http.HttpHeaders;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalCookie;
import io.github.caiomatenorio.tasklist_service.convention.ConventionalResponse;
import io.github.caiomatenorio.tasklist_service.convention.SuccessResponse;
import io.github.caiomatenorio.tasklist_service.service.UserService;
import io.github.caiomatenorio.tasklist_service.util.CookieUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/logout")
@RequiredArgsConstructor
public class LogoutController {
    private final UserService userService;
    private final CookieUtil cookieUtil;

    @PostMapping
    public ResponseEntity<ConventionalResponse> logout() {
        Set<ConventionalCookie> deletedCookies = userService.logout();
        HttpHeaders headers = cookieUtil.toHttpHeaders(deletedCookies);

        return new SuccessResponse<>(204).toResponseEntity(headers);
    }
}
