package com.springlog.repetitivelearning.exception;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(OwnerNotFoundException.class)
  public ProblemDetail ownerNotFoundException(OwnerNotFoundException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.NOT_FOUND, e.getMessage()
    );

    problemDetail.setTitle("사용자를 찾지 못함");
    problemDetail.setProperty("발생 시간", Instant.now().atZone(
        ZoneId.of("Asia/Seoul")));
    return problemDetail;
  }

  @ExceptionHandler(ActivityNotFoundException.class)
  public ProblemDetail activityNotFoundException(ActivityNotFoundException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.NOT_FOUND, e.getMessage()
    );

    problemDetail.setTitle("활동을 찾지 못함");
    problemDetail.setProperty("발생 시간",  Instant.now().atZone(ZoneId.of("Asia/Seoul")));
  return problemDetail;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidationException(MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, "필드 검증에 실패했습니다."
    );

    e.getBindingResult().getFieldErrors().forEach( (fieldError) -> {
      errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
    );

    problemDetail.setTitle("필드 검증 실패");
    problemDetail.setProperty("발생 시간", Instant.now().atZone(
        ZoneId.of("Asia/Seoul")));
    problemDetail.setProperty("errors", errors);

    return problemDetail;
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleException(Exception e) {
    log.error("예상 못한 예외 발생", e);

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.INTERNAL_SERVER_ERROR, "예상 못한 예외"
    );
    problemDetail.setTitle("예상 못한 예외");
    problemDetail.setProperty("발생 시간",  Instant.now().atZone(
        ZoneId.of("Asia/Seoul")));

    return problemDetail;
  }

}
