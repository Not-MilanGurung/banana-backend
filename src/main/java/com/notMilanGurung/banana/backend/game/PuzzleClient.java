package com.notMilanGurung.banana.backend.game;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.notMilanGurung.banana.backend.exception.ErrorResponse;

import tools.jackson.databind.ObjectMapper;

@Component
public class PuzzleClient {

  private final RestClient client;
  private final ObjectMapper mapper;

  public PuzzleClient(@Value("${banana.api.url}") String url, ObjectMapper mapper) {
    var factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(3000);
    factory.setReadTimeout(5000);
    this.client = RestClient.builder().requestFactory(factory).baseUrl(url).build();
    this.mapper = mapper;
  }

  public Puzzle fetch() throws ErrorResponse {
    try {
      String body = client.get().retrieve().body(String.class);
      return mapper.readValue(body, Puzzle.class);
    } catch (Exception e) {
      throw new ErrorResponse(HttpStatus.BAD_GATEWAY, "Puzzle service unavailable");
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Puzzle(String question, int solution) {}
}