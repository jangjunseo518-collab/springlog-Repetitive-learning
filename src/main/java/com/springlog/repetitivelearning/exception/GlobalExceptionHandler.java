package com.springlog.repetitivelearning.exception;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
    List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, "필드 검증에 실패했습니다."
    );

    for (FieldError fieldError : fieldErrors) {
      if ("typeMismatch".equals(fieldError.getCode())) {
        errors.put(fieldError.getField(), "유효하지 않은 값이 입력되었습니다.");
      } else {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
      }
    }

    problemDetail.setTitle("필드 검증 실패");
    problemDetail.setProperty("발생 시간", Instant.now().atZone(
        ZoneId.of("Asia/Seoul")));
    problemDetail.setProperty("errors", errors);

    return problemDetail;
  }

  @ExceptionHandler(PagingRequestException.class)
  public ProblemDetail pagingRequestException(PagingRequestException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, e.getMessage()
    );
    problemDetail.setTitle("지원하지 않는 페이징 기준");
    problemDetail.setProperty("발생 시간",  Instant.now().atZone(ZoneId.of("Asia/Seoul")));

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

  @ExceptionHandler(PageValidateFailureException.class)
  public ProblemDetail pageValidateFailure(PageValidateFailureException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, e.getMessage()
    );
    problemDetail.setTitle("page 음수 입력");
    problemDetail.setProperty("발생 시간",   Instant.now().atZone(ZoneId.of("Asia/Seoul")));

    return problemDetail;
  }

  @ExceptionHandler(VisibilityNotPublicException.class)
  public ProblemDetail visibilityNotPublicException(VisibilityNotPublicException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, e.getMessage()
    );
    problemDetail.setTitle("공개 페이지 조회에서 비공개 활동 페이지 조회 시도");
    problemDetail.setProperty("발생 시간", Instant.now().atZone(
        ZoneId.of("Asia/Seoul")));

    return problemDetail;
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ProblemDetail methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, "유효하지 않은 값 입니다."
    );
    problemDetail.setTitle("유효하지 않은 값");
    problemDetail.setProperty("발생 시간", Instant.now().atZone(
        ZoneId.of("Asia/Seoul")));
    problemDetail.setProperty("입력 값",  e.getValue());
    return problemDetail;
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ProblemDetail missingServletRequestParameterException(
      MissingServletRequestParameterException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, "필수 필드가 비어있습니다."
    );

    problemDetail.setTitle("필수 필드 누락");
    problemDetail.setProperty("발생 시간", Instant.now().atZone(ZoneId.of("Asia/Seoul")));
    problemDetail.setProperty("누락 필드" , e.getParameterName());

    return problemDetail;
  }

}
