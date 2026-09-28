package com.springlog.repetitivelearning.exception;

public class InvalidSortFieldException extends RuntimeException {

  public InvalidSortFieldException(String requestSort) {
    super("지원하지 않는 정렬 조건입니다.");
  }
}
