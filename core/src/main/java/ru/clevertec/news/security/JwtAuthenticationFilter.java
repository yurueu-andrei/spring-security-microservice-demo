package ru.clevertec.news.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;
import ru.clevertec.UserDto;
import ru.clevertec.news.exception.CustomException;

import java.io.IOException;

/**
 * Authentication filter class
 *
 * @author Yuryeu Andrei
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final WebClient webClient;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        UserDto userDto = webClient.post()
                .uri("http://localhost:8081/auth/users")
                .header(HttpHeaders.AUTHORIZATION, header)
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorComplete()
                .block();

        if (userDto == null) {
            response.setStatus(400);
            response.getWriter().write("Invalid token");
            return;
        }

        UserDetails userDetails = User.builder()
                .username(userDto.getUsername())
                .password("")
                .authorities(
                        userDto.getAuthorities()
                                .stream()
                                .map(SimpleGrantedAuthority::new)
                                .toList()
                )
                .build();

        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.authenticated(
                userDetails, null, userDetails.getAuthorities()
        );
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);

        filterChain.doFilter(request, response);
    }
}
