package ru.clevertec.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clevertec.security.dto.AuthenticationResponseDto;
import ru.clevertec.security.dto.LoginRequest;
import ru.clevertec.security.dto.RegisterRequest;
import ru.clevertec.security.entity.User;
import ru.clevertec.security.exception.CustomException;
import ru.clevertec.security.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDto register(RegisterRequest request) {
        User user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(request.getRoles())
                .build();
        try {
            userRepository.save(user);
        } catch (Exception ex) {
            throw new CustomException("Username is already exists", HttpStatus.BAD_REQUEST);
        }
        String token = jwtService.generateToken(user);
        return new AuthenticationResponseDto(token);
    }

    public AuthenticationResponseDto login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException("Username does not exist", HttpStatus.BAD_REQUEST));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                request.getPassword()));
        String token = jwtService.generateToken(user);
        return new AuthenticationResponseDto(token);
    }
}
