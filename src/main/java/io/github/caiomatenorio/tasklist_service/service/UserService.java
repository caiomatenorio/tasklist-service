package io.github.caiomatenorio.tasklist_service.service;

import org.springframework.stereotype.Service;

import io.github.caiomatenorio.tasklist_service.dto.request.SignupRequest;
import io.github.caiomatenorio.tasklist_service.entity.User;
import io.github.caiomatenorio.tasklist_service.exception.UsernameAlreadyInUseException;
import io.github.caiomatenorio.tasklist_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void signup(SignupRequest request) throws UsernameAlreadyInUseException {
        userRepository.findByUsername(request.username())
                .ifPresent(user -> {
                    throw new UsernameAlreadyInUseException(user.getUsername());
                });

        User user = new User(request.name(), request.username(), request.password());
        userRepository.save(user);
    }
}
