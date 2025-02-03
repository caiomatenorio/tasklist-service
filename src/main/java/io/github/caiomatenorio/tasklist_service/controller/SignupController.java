package io.github.caiomatenorio.tasklist_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.caiomatenorio.tasklist_service.dto.ConventionalResponse;
import io.github.caiomatenorio.tasklist_service.dto.ConventionalResponseEntity;
import io.github.caiomatenorio.tasklist_service.dto.request.SignupRequest;
import io.github.caiomatenorio.tasklist_service.exception.UsernameAlreadyInUseException;
import io.github.caiomatenorio.tasklist_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {
    private final UserService userService;

    @PostMapping
    public ConventionalResponseEntity signup(@Valid @RequestBody SignupRequest request) {
        try {
            userService.signup(request);
            return new ConventionalResponse.Success<>(201, "User created").toEntity();
        } catch (UsernameAlreadyInUseException e) {
            return new ConventionalResponse.Error(409, e.getMessage()).toEntity();
        } catch (Exception e) {
            return new ConventionalResponse.Error(500, "An internal error occurred").toEntity();
        }
    }
}
