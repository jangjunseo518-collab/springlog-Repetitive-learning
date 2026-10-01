package com.springlog.repetitivelearning.exception;

public class NotValidPageException extends RuntimeException {

  public NotValidPageException(int page) {
    super("page는 음수일 수 없습니다. 입력 값: "+page);
  }
}
