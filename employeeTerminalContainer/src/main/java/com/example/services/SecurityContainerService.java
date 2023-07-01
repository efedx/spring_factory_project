package com.example.services;

import com.example.dto.JwtDto;
import com.example.dto.JwtGenerationRequestDto;
import com.example.dto.LoginRequestDto;
import com.example.exceptions.CustomSecurityException;
import com.example.exceptions.SecurityExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SecurityContainerService {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    //-----------------------------------------------------------------------------------------------

    /**
     *Performs JWT validation by sending a request to the specified URL with the provided authorization header.
     *If the validation response indicates an error, a CustomSecurityException is thrown.
     *
     *@param authorizationHeader The authorization header containing the JWT token.
     *@param url The URL to which the validation request is sent.
     *@throws JsonProcessingException If an error occurs during JSON processing.
     *@throws CustomSecurityException If the validation response indicates an error.
     */
    public void jwtValidation(String authorizationHeader, String url) throws JsonProcessingException {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", authorizationHeader);
        HttpEntity<JwtGenerationRequestDto> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> validationResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//       try {
//           ResponseEntity<String> validationResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//       } catch (HttpClientErrorException e) {
//           String body = e.getResponseBodyAsString();
//           int i = 6;
//       }

//        if(validationResponse.getStatusCode().isError()) {
//
//            SecurityExceptionResponse securityExceptionResponse = objectMapper.convertValue(validationResponse.getBody(), SecurityExceptionResponse.class);
//            HttpHeaders responseHttpHeaders = validationResponse.getHeaders();
//            HttpStatusCode httpStatusCode = validationResponse.getStatusCode();
//            String message = securityExceptionResponse.message();
//            throw new CustomSecurityException(message, httpStatusCode, responseHttpHeaders);
//        }
    }

    //-----------------------------------------------------------------------------------------------

    /**
     *Performs login validation by sending a request to the specified URL with the provided login request data.
     *If the validation response indicates a successful login, a ResponseEntity containing the JWT token is returned.
     *If the validation response indicates an error, a CustomSecurityException is thrown.
     *
     *@param loginRequestDto The login request data containing the username and password.
     *@param url The URL to which the validation request is sent.
     *@return A ResponseEntity containing the JWT token if the login is successful.
     *@throws CustomSecurityException If the validation response indicates an error.
     */
    public ResponseEntity<JwtDto> login (LoginRequestDto loginRequestDto, String url) {

        HttpEntity<LoginRequestDto> requestEntity = new HttpEntity<>(loginRequestDto);
        ResponseEntity<Object> jwtResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Object.class);

        if(jwtResponse.getStatusCode().is2xxSuccessful()) {

            HttpStatusCode statusCode = jwtResponse.getStatusCode();
            HttpHeaders headers = jwtResponse.getHeaders();
            JwtDto jwtDto = objectMapper.convertValue(jwtResponse.getBody(), JwtDto.class);

            return new ResponseEntity<>(jwtDto, headers, statusCode);
        }

        else {
            SecurityExceptionResponse securityExceptionResponse = objectMapper.convertValue(jwtResponse.getBody(), SecurityExceptionResponse.class);
            HttpStatusCode httpStatusCode = jwtResponse.getStatusCode();
            HttpHeaders responseHttpHeaders = jwtResponse.getHeaders();
            String message = securityExceptionResponse.message();
            throw new CustomSecurityException(message, httpStatusCode, responseHttpHeaders);
        }
    }

    //-----------------------------------------------------------------------------------------------

    private SecurityExceptionResponse JsonToSecurityExceptionResponse(ResponseEntity<Object> responseEntity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SecurityExceptionResponse securityExceptionResponse = objectMapper.readValue(responseEntity.getBody().toString(), SecurityExceptionResponse.class);
        return securityExceptionResponse;
    }

}
