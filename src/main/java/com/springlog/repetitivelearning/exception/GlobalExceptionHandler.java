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

    ProblemDetail problemDetail = problem(ErrorCode.INVALID_INPUT.getStatus(), ErrorCode.INVALID_INPUT.getCode(),
        "필드 검증 실패", "필드 검증에 실패했습니다.");

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

    problemDetail.setProperty("errors", errors);

    return problemDetail;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail illegalArgumentException(IllegalArgumentException e) {
    ProblemDetail problemDetail = problem(
        ErrorCode.INVALID_INPUT.getStatus(),  ErrorCode.INVALID_INPUT.getCode(),
         "잘못된 입력" ,e.getMessage()
    );

    return problemDetail;
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleException(Exception e) {
    log.error("예상 못한 예외 발생", e);

    ProblemDetail problemDetail = problem(
        ErrorCode.INTERNAL_ERROR.getStatus(), ErrorCode.INTERNAL_ERROR.getCode(),
        "예상 못한 예외", ErrorCode.INTERNAL_ERROR.getDefaultMessage()
    );

    return problemDetail;
  }


  @ExceptionHandler(BusinessException.class)
  public ProblemDetail handlerBusinessException(BusinessException e) {
    ErrorCode errorCode = e.getErrorCode();

    if(errorCode.getStatus().is5xxServerError()) {
      log.error("[{}] {}", errorCode.getCode(), e.getMessage(), e);   // 스택 트레이스 포함
    } else {
      log.warn("[{}] {}", errorCode.getCode(), e.getMessage());        // 한 줄만
    }

    return problem(
        errorCode.getStatus(), errorCode.getCode(),
        errorCode.getDefaultMessage(), e.getMessage()
    );
  }

  private ProblemDetail problem(HttpStatus status, String code, String title, String detail) {

    ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
    pd.setTitle(title);
    pd.setProperty("code", code);
    pd.setProperty("발생 시간", Instant.now().atZone(ZoneId.of("Asia/Seoul")));
    return pd;
  }

}
