package ru.clevertec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionMessage {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
