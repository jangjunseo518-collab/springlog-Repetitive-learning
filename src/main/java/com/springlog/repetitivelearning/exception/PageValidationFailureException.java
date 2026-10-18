package com.springlog.repetitivelearning.exception;

public class PageValidationFailureException extends BusinessException {

  public PageValidationFailureException(int page) {
    super(ErrorCode.PAGE_VALIDATION_FAILURE, "page는 음수를 입력할 수 없습니다. 입력값: "+page);
  }
}
