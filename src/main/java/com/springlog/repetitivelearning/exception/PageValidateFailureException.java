package com.springlog.repetitivelearning.exception;

public class PageValidateFailureException extends RuntimeException {

  public PageValidateFailureException(int page) {
    super("페이지는 0이상 수만 입력 가능합니다. page: " + page);
  }
}
