package com.viaje.market.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RestUtil {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;
    String API_KEY = "46sfy6etc12400cg1uxn58oexi0y0uffgn587y23ncganfqig9";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RestUtil(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public JsonNode getRequestWithToken(String url, String signature) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("sign", signature);
        headers.add("x-api-key", API_KEY);
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, httpEntity, String.class);
        JsonNode responseBody = objectMapper.readTree(response.getBody());

//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return responseBody;
    }

    public ResponseEntity<?> getWithToken(String url, String signature) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("sign", signature);
        headers.add("x-api-key", API_KEY);
        HttpEntity httpEntity = new HttpEntity(headers);


        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, httpEntity, String.class);

//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response;
    }

    public JsonNode postWithBody(String url, String signature, Object body) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("sign", signature);
        headers.add("x-api-key", API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Gson gson = new Gson();
        String stringObject = gson.toJson(body);

        HttpEntity httpEntity = new HttpEntity(stringObject, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                url, httpEntity, String.class, body);
        JsonNode myBody = objectMapper.readTree(response.getBody());
//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return myBody;
    }
}