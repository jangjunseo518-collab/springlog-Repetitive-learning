package com.springlog.repetitivelearning.exception;

public class PageValidationFailureException extends RuntimeException {

  public PageValidationFailureException(int page) {
    super("페이지는 음수일 수 없습니다.");
  }
}
