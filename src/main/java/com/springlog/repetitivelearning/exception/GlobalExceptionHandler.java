package com.springlog.repetitivelearning.exception;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidationException(MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();

    ErrorCode errorCode = ErrorCode.INVALID_INPUT;

    ProblemDetail pd = problem(errorCode.getCode(), errorCode.getHttpStatus(),
        "필드 검증 필패", "필드 검증에 실패했습니다.");

    e.getBindingResult().getFieldErrors().forEach( (fieldError) -> {
      boolean contains = fieldError.contains(TypeMismatchException.class);
      if(contains) {
        TypeMismatchException unwrap = fieldError.unwrap(TypeMismatchException.class);
        Object value = unwrap.getValue();
        Class<?> requiredType = unwrap.getRequiredType();
        Object[] enumConstants = requiredType != null? requiredType.getEnumConstants(): null;
        String field = fieldError.getField();

        errors.put(field, field+"필드에 잘못된 값이 입력되었습니다. 입렧값: "
                                 +value+" | 하용값: "+ Arrays.toString(enumConstants));
       } else {
          errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
      }
    );
    pd.setProperty("errors", errors);
    return pd;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail illegalArgumentException(IllegalArgumentException e) {
   ErrorCode errorCode = ErrorCode.INVALID_INPUT;

    ProblemDetail pd = problem(errorCode.getCode(), errorCode.getHttpStatus(),
        "잘못된 입력값", e.getMessage());
    return pd;
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleException(Exception e) {
    log.error("예상 못한 예외 발생", e);

   ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
    ProblemDetail pd = problem(errorCode.getCode(), errorCode.getHttpStatus(),
        "예상치 못한 예외", errorCode.getDefaultMessage());

    return pd;
  }

  @ExceptionHandler(BusinessException.class)
  public ProblemDetail handleBusinessException(BusinessException e) {
    ErrorCode errorCode = e.getErrorCode();

    if(errorCode.getHttpStatus().is5xxServerError()) {
      log.error("[{}] {}", errorCode, e.getMessage(), e);
    } else  {
      log.warn("[{}] {}", errorCode, e.getMessage());
    }

    ProblemDetail pd = problem(errorCode.getCode(), errorCode.getHttpStatus(),
        errorCode.getDefaultMessage(), e.getMessage());

    return pd;
  }

  private ProblemDetail problem(String code, HttpStatus status,
                               String title, String detail) {
    ProblemDetail pd = ProblemDetail.forStatusAndDetail(
        status, detail
    );

    pd.setTitle(title);
    pd.setProperty("code", code);
    pd.setProperty("발생 시간", Instant.now().atZone(
        ZoneId.of("Asia/Seoul")
    ));
    return pd;
  }

}
