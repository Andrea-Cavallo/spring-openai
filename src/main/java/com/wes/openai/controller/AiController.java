package com.wes.openai.controller;

import com.wes.openai.model.RequestDTO;
import com.wes.openai.model.ResponseDTO;
import com.wes.openai.service.IOpenAiService;
import com.wes.openai.utils.OpenAIUtility;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import java.util.*;
import java.util.concurrent.TimeUnit;
import io.micrometer.core.instrument.MeterRegistry;

import static com.wes.openai.utils.Constants.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class AiController {

    private final IOpenAiService IOpenAiService;
    private final MeterRegistry meterRegistry;

    @PostMapping("/v1/request")
    @ApiOperation(value = "Ask a question to the AI", notes = "This endpoint allows you to ask a question to the AI and get a response.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved response", response = ResponseDTO.class),
            @ApiResponse(code = 400, message = "Invalid input"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<ResponseDTO> askToAi(@Valid @RequestBody RequestDTO request) {
        long startTime = System.currentTimeMillis();
        meterRegistry.counter("requests.total").increment();
        HttpHeaders headers = new HttpHeaders();
        headers.add("correlation-id", UUID.randomUUID().toString());

        try {
            String prompt = request.getMessage();
            log.info(RECEIVED_REQUEST_TO_GENERATE_EMBEDDING_FOR_MESSAGE, prompt);
            String responseFromOpenAI = IOpenAiService.getResponseFromOpenAI(prompt);
            long duration = System.currentTimeMillis() - startTime;
            meterRegistry.timer("requests.latency").record(duration, TimeUnit.MILLISECONDS);
            log.info("Request processed in {} ms", duration);


            ResponseDTO responseDTO = OpenAIUtility.createResponse(responseFromOpenAI, HttpStatus.OK.value(), Collections.emptyMap());
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(responseDTO);
        } catch (ResponseStatusException e) {
            long duration = System.currentTimeMillis() - startTime;
            meterRegistry.timer("requests.latency").record(duration, TimeUnit.MILLISECONDS);
            log.error(BAD_REQUEST + " processed in {} ms", duration, e.getReason(), e);

            ResponseDTO responseDTO = OpenAIUtility.createResponse("", HttpStatus.BAD_REQUEST.value(), Map.of(MESSAGE, Objects.requireNonNull(e.getReason())));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .headers(headers)
                    .body(responseDTO);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            meterRegistry.timer("requests.latency").record(duration, TimeUnit.MILLISECONDS);
            log.error(ERROR_WHILE_GENERATING_EMBEDDING_FOR_MESSAGE + " processed in {} ms", duration, request.getMessage(), e);

            ResponseDTO responseDTO = OpenAIUtility.createResponse("", HttpStatus.INTERNAL_SERVER_ERROR.value(), Map.of(MESSAGE, e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(headers)
                    .body(responseDTO);
        }
    }
}

