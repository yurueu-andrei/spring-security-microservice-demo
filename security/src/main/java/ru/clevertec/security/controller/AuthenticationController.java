package ru.clevertec.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.UserDto;
import ru.clevertec.security.dto.AuthenticationResponseDto;
import ru.clevertec.security.dto.LoginRequest;
import ru.clevertec.security.dto.RegisterRequest;
import ru.clevertec.security.entity.Role;
import ru.clevertec.security.entity.User;
import ru.clevertec.security.exception.CustomException;
import ru.clevertec.security.repository.UserRepository;
import ru.clevertec.security.service.AuthenticationService;
import ru.clevertec.security.service.JwtService;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/users")
    @SneakyThrows
    // можно возвращать все, что угодно, что понадобится для проверок в другом микросервисе
    public ResponseEntity<UserDto> findUserByToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        String rawToken = header.substring(7);
        String username = jwtService.extractUsername(rawToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Username does not exist", HttpStatus.BAD_REQUEST));
        jwtService.validateToken(rawToken, user);
        List<String> authorities = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        UserDto userDto = new UserDto(username, authorities, roles);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}

