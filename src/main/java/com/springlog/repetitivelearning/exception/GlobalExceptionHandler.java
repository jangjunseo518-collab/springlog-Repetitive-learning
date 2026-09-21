package com.springlog.repetitivelearning.exception;

import java.time.Instant;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(ActivityNotFoundException.class)
  public ProblemDetail activityNotFoundException(ActivityNotFoundException e) {

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.NOT_FOUND, e.getMessage());
    problemDetail.setTitle("활동을 찾을 수 없음");
    problemDetail.setProperty("발생 시간", Instant.now());

    return problemDetail;
  }

  @ExceptionHandler(OwnerNotFoundException.class)
  public ProblemDetail ownerNotFoundException(OwnerNotFoundException e) {

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.NOT_FOUND, e.getMessage());
    problemDetail.setTitle("사용자를 찾을 수 없음");
    problemDetail.setProperty("발생 시간", Instant.now());

    return problemDetail;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidationException(
      MethodArgumentNotValidException e) {
    HashMap<String, String> fieldErrorMap = new HashMap<>();

    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
         fieldErrorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
    }

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, "요청 본문의 일부 필드가 유효하지 않음");
    problemDetail.setProperty("errors",  fieldErrorMap);
    problemDetail.setTitle("검증 오류");
    problemDetail.setProperty("발생 시간", Instant.now());

    return problemDetail;

  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleException(Exception e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 예외 발생"
    );
    log.error("예상 못한 서버 오류", e);
    problemDetail.setTitle("예상 못한 예외");
    problemDetail.setProperty("발생 시간",  Instant.now());

    return problemDetail;
  }


}
