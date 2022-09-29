package com.atm.config.error;

import com.atm.exception.NotEnoughCashException;
import com.atm.exception.NotValidUserException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

  private ObjectMapper om =
      new ObjectMapper()
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .setSerializationInclusion(JsonInclude.Include.NON_NULL);

  @ExceptionHandler({JwtException.class})
  public final ResponseEntity<Object> handleJwtException(
      JwtException th, WebRequest request) {
    log.error(th.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ExceptionDetails(HttpStatus.BAD_REQUEST, th.getMessage()));
  }

  @ExceptionHandler({NotValidUserException.class})
  public final ResponseEntity<Object> handleNotValidUserException(
      NotValidUserException th, WebRequest request) {
    log.error(th.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ExceptionDetails(HttpStatus.BAD_REQUEST, th.getMessage()));
  }

  @ExceptionHandler({ConstraintViolationException.class})

  public final ResponseEntity<Object> handleConstraintException(
      ConstraintViolationException th, WebRequest request) {
    log.error(th.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ExceptionDetails(HttpStatus.BAD_REQUEST, th.getMessage()));
  }

  @ExceptionHandler({NotEnoughCashException.class})
  public final ResponseEntity<Object> handleNotEnoughCashException(
      NotEnoughCashException th, WebRequest request) {
    log.error(th.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ExceptionDetails(HttpStatus.FORBIDDEN, th.getMessage()));
  }

  @ExceptionHandler({Exception.class})
  public final ResponseEntity<Object> handleGenericException(
      Exception th, WebRequest request) {
    log.error(th.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ExceptionDetails(HttpStatus.INTERNAL_SERVER_ERROR, th.getMessage()));
  }
}
