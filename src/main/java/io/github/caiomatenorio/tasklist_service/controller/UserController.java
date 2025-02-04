package io.github.caiomatenorio.tasklist_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalResponseBody;
import io.github.caiomatenorio.tasklist_service.dto.response.GetUserBasicDataResponse;
import io.github.caiomatenorio.tasklist_service.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/basic")
    public ResponseEntity<ConventionalResponseBody> getBasicData() {
        GetUserBasicDataResponse userBasicData = userService.getUserBasicData();
        return new ConventionalResponseBody.Success<>(200, userBasicData).toResponseEntity();
    }
}
