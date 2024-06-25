package com.wes.openai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class ResponseDTO {
    private String output;
    private Map<String, Object> exception;
    private Integer status;

}
