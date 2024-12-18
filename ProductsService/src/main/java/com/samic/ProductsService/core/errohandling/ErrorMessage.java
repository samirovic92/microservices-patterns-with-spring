package com.samic.ProductsService.core.errohandling;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class ErrorMessage {
    private final LocalDateTime timestamp;
    private final String message;
}
