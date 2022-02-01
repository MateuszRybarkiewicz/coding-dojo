package com.assignment.spring.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {
    private int statusCode;
    private String message;
}
