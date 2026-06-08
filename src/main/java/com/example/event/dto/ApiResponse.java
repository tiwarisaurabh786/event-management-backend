package com.example.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
}
