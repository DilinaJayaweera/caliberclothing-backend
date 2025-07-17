package com.example.caliberclothing.exception;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private LocalDateTime timestamp;
    private int status;
}
