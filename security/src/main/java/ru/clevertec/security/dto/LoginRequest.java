package ru.clevertec.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * DTO class for <b>Logging in</b> with the following fields:
 * username, password
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Length(min = 6)
    private String password;
}
