package com.springlog.repetitivelearning.exception;

public class PagingRequestException extends RuntimeException {

  public PagingRequestException(String sort) {
    super("지원하지 않는 페이지 정렬 기준 sort: " + sort);
  }
}
