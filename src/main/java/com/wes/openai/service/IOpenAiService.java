package com.wes.openai.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IOpenAiService {


     String getResponseFromOpenAI(String prompt) throws JsonProcessingException;

}
