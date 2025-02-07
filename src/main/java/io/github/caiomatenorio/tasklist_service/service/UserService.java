package io.github.caiomatenorio.tasklist_service.service;

import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalCookie;
import io.github.caiomatenorio.tasklist_service.dto.request.LoginRequest;
import io.github.caiomatenorio.tasklist_service.dto.request.SignupRequest;
import io.github.caiomatenorio.tasklist_service.dto.request.UpdateNameRequest;
import io.github.caiomatenorio.tasklist_service.dto.request.UpdatePasswordRequest;
import io.github.caiomatenorio.tasklist_service.dto.request.UpdateUsernameRequest;
import io.github.caiomatenorio.tasklist_service.dto.response.CurrentUserResponse;
import io.github.caiomatenorio.tasklist_service.exception.InvalidPasswordException;
import io.github.caiomatenorio.tasklist_service.exception.InvalidUsernameOrPasswordException;
import io.github.caiomatenorio.tasklist_service.exception.UnauthorizedException;
import io.github.caiomatenorio.tasklist_service.exception.UsernameAlreadyInUseException;
import io.github.caiomatenorio.tasklist_service.model.User;
import io.github.caiomatenorio.tasklist_service.repository.UserRepository;
import io.github.caiomatenorio.tasklist_service.security.token.AuthenticationTokenDetails;
import io.github.caiomatenorio.tasklist_service.security.util.AuthUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final AuthUtil authUtil;

    @Transactional
    public Set<ConventionalCookie> login(LoginRequest request) throws InvalidUsernameOrPasswordException {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(InvalidUsernameOrPasswordException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new InvalidUsernameOrPasswordException();

        UUID sessionId = sessionService.createSession(user);
        return sessionService.createSessionCookies(sessionId);
    }

    @Transactional
    public void signup(SignupRequest request) throws UsernameAlreadyInUseException {
        userRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new UsernameAlreadyInUseException();
        });

        String hashedPassword = passwordEncoder.encode(request.password());

        User user = new User(request.name(), request.username(), hashedPassword);
        userRepository.save(user);
    }

    @Transactional
    public Set<ConventionalCookie> logout() {
        UUID sessionId = authUtil.getCurrentUserDetails().sessionId();
        sessionService.deleteSession(sessionId);

        return sessionService.deleteSessionCookies();
    }

    public CurrentUserResponse getCurrentUserData() throws UnauthorizedException {
        return new CurrentUserResponse(
                authUtil.getCurrentUserDetails().userId(),
                authUtil.getCurrentUsername(),
                authUtil.getCurrentUserDetails().name());
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) throws UnauthorizedException {
        return userRepository.findByUsername(username).orElseThrow(UnauthorizedException::new);
    }

    @Transactional
    public Set<ConventionalCookie> updateCurrentName(UpdateNameRequest request) throws UnauthorizedException {
        AuthenticationTokenDetails currentUserDetails = authUtil.getCurrentUserDetails();
        User user = userRepository.findById(currentUserDetails.userId()).orElseThrow(UnauthorizedException::new);

        user.setName(request.name());
        userRepository.save(user);

        return sessionService.createSessionCookies(currentUserDetails.sessionId());
    }

    @Transactional
    public Set<ConventionalCookie> updateCurrentUsername(UpdateUsernameRequest request)
            throws UnauthorizedException, InvalidPasswordException, UsernameAlreadyInUseException {
        AuthenticationTokenDetails currentUserDetails = authUtil.getCurrentUserDetails();
        User user = userRepository.findById(currentUserDetails.userId()).orElseThrow(UnauthorizedException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new InvalidPasswordException();

        userRepository.findByUsername(request.username()).ifPresent(existingUser -> {
            throw new UsernameAlreadyInUseException();
        });

        user.setUsername(request.username());
        userRepository.save(user);

        return sessionService.createSessionCookies(currentUserDetails.sessionId());
    }

    @Transactional
    public void updateCurrentPassword(UpdatePasswordRequest request) throws UnauthorizedException {
        AuthenticationTokenDetails currentUserDetails = authUtil.getCurrentUserDetails();
        User user = userRepository.findById(currentUserDetails.userId()).orElseThrow(UnauthorizedException::new);

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword()))
            throw new InvalidPasswordException();

        String hashedPassword = passwordEncoder.encode(request.newPassword());

        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    @Transactional
    public Set<ConventionalCookie> deleteCurrentUser() {
        UUID userId = authUtil.getCurrentUserDetails().userId();
        userRepository.deleteById(userId);

        return sessionService.deleteSessionCookies();
    }
}
