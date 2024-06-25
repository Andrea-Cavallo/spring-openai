package com.wes.openai.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;



@Data
public class RequestDTO {
    @NotEmpty(message = "Message cannot be empty")
    private String message;
}
