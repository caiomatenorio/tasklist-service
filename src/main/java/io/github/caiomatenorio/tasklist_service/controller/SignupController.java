package io.github.caiomatenorio.tasklist_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalResponseBody;
import io.github.caiomatenorio.tasklist_service.dto.request.SignupRequest;
import io.github.caiomatenorio.tasklist_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ConventionalResponseBody> signup(@Valid @RequestBody SignupRequest request) {
        userService.signup(request);

        return new ConventionalResponseBody.Success<>(201, "User " + request.username() + " created")
                .toResponseEntity();
    }
}
