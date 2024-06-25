package com.wes.openai.utils;

import com.wes.openai.model.ResponseDTO;

import java.util.HashMap;
import java.util.Map;
import static com.wes.openai.utils.Constants.*;
/**
 * The OpenAIUtility class provides utility methods for creating a response object.
 */
public class OpenAIUtility {

    public static ResponseDTO createResponse(String output, int status, Map<String, Object> exception) {

        return  ResponseDTO.builder().output(output).exception(exception).status(status).build();
    }
}
