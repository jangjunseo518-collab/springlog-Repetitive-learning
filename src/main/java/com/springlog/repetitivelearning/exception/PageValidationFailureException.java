package com.springlog.repetitivelearning.exception;

public class PageValidationFailureException extends RuntimeException {

  public PageValidationFailureException(int page) {
    super("page는 음수를 입력할 수 없습니다. 입력값: "+page);
  }
}
