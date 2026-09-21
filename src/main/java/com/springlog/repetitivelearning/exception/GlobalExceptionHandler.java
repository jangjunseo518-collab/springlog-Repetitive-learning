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
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
        e.getMessage());
    problemDetail.setTitle("사용자를 찾을 수 없음");
    problemDetail.setProperty("발생 시간", Instant.now()
        .atZone(ZoneId.of("Asia/Seoul")));

    return problemDetail;
  }

  @ExceptionHandler(ActivityNotFoundException.class)
  public ProblemDetail activityNotFoundException(ActivityNotFoundException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
        e.getMessage());
    problemDetail.setTitle("활동을 찾을 수 없음");
    problemDetail.setProperty("발생 시간", Instant.now()
        .atZone(ZoneId.of("Asia/Seoul")));

    return problemDetail;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidation(MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();

    e.getBindingResult().getFieldErrors().forEach((error) -> {
      errors.put(error.getField(), error.getDefaultMessage());
    });

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, "일부 필드의 유효성 검증이 실패 했습니다."
    );

    problemDetail.setTitle("필드 유효성 검증 실패");
    problemDetail.setProperty("발생 시간", Instant.now().atZone(ZoneId.of("Asia/Seoul")));
    problemDetail.setProperty("errors", errors);
    return problemDetail;
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail exception(Exception e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 예외가 발생 했습니다."
    );

    log.error("예상 못한 서버 오류", e);
    problemDetail.setTitle("예상 못한 서버 오류");
    problemDetail.setProperty("발생 시간", Instant.now().atZone(ZoneId.of("Asia/Seoul")) );

    return problemDetail;
  }



}
