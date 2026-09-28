package com.springlog.repetitivelearning.exception;

public class PagingRequestException extends RuntimeException {

  public PagingRequestException(String sort) {
    super("유효하지 않은 정렬 조건입니다.");
  }
}
