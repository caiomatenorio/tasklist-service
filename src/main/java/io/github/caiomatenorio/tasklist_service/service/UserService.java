package io.github.caiomatenorio.tasklist_service.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.caiomatenorio.tasklist_service.dto.request.LoginRequest;
import io.github.caiomatenorio.tasklist_service.dto.request.SignupRequest;
import io.github.caiomatenorio.tasklist_service.dto.response.GetCurrentUserDataResponse;
import io.github.caiomatenorio.tasklist_service.exception.InvalidUsernameOrPasswordException;
import io.github.caiomatenorio.tasklist_service.exception.UnauthorizedException;
import io.github.caiomatenorio.tasklist_service.exception.UsernameAlreadyInUseException;
import io.github.caiomatenorio.tasklist_service.model.User;
import io.github.caiomatenorio.tasklist_service.repository.UserRepository;
import io.github.caiomatenorio.tasklist_service.security.util.AuthUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final AuthUtil authUtil;

    public UUID login(LoginRequest request) throws InvalidUsernameOrPasswordException {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(InvalidUsernameOrPasswordException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new InvalidUsernameOrPasswordException();

        return sessionService.createSession(user);
    }

    public void signup(SignupRequest request) throws UsernameAlreadyInUseException {
        userRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new UsernameAlreadyInUseException();
        });

        String hashedPassword = passwordEncoder.encode(request.password());

        User user = new User(request.name(), request.username(), hashedPassword);
        userRepository.save(user);
    }

    public void logout() {
        UUID sessionId = authUtil.getCurrentUserDetails().sessionId();
        sessionService.deleteSession(sessionId);
    }

    public GetCurrentUserDataResponse getCurrentUserData() throws UnauthorizedException, IllegalArgumentException {
        return new GetCurrentUserDataResponse(
                authUtil.getCurrentUserDetails().userId(),
                authUtil.getCurrentUsername(),
                authUtil.getCurrentUserDetails().name());
    }
}
