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

    problemDetail.setTitle("필드 검증 실패");
    problemDetail.setProperty("발생 시간", Instant.now().atZone(
        ZoneId.of("Asia/Seoul")));
    problemDetail.setProperty("errors", errors);

    return problemDetail;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail handleIllegalArgument(IllegalArgumentException e) {
    log.warn("잘못된 요청: {}", e.getMessage());
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, e.getMessage()
    );
    problemDetail.setTitle("잘 못된 요청");
    problemDetail.setProperty("발생 시간", Instant.now().atZone(
        ZoneId.of("Asia/Seoul")
    ));
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

  @ExceptionHandler(VisibilityNotPublicException.class)
  public ProblemDetail visibilityNotPublicException(VisibilityNotPublicException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, e.getMessage()
    );

    problemDetail.setTitle("공개활동 조회로 공개활동 외 활동 조회");
    problemDetail.setProperty("발생 시간",  Instant.now().atZone(
        ZoneId.of("Asia/Seoul")
    ));

    return problemDetail;
  }

  @ExceptionHandler(PageValidationFailureException.class)
  public ProblemDetail pageValidationFailureException(PageValidationFailureException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, e.getMessage()
    );

    problemDetail.setTitle("page 음수 입력");
    problemDetail.setProperty("발생 시간",  Instant.now().atZone(
        ZoneId.of("Asia/Seoul")
    ));
    return problemDetail;
  }

  @ExceptionHandler(InvalidSortFieldException.class)
  public ProblemDetail invalidSortFieldException(InvalidSortFieldException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, e.getMessage()
    );
    problemDetail.setTitle("지원하지 않는 정렬 기준");
    problemDetail.setProperty("발생 시간", Instant.now().atZone(
        ZoneId.of("Asia/Seoul")
    ));
    return problemDetail;
  }

}
