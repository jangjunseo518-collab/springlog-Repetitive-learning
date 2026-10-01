package com.springlog.repetitivelearning.exception;

public class NotValidSortFieldException extends RuntimeException {

  public NotValidSortFieldException(String sort) {
    super("해당 정렬 기준은 제원하지 않는 정렬 기준입니다.입력 값: "+ sort);
  }
}
