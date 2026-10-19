package com.springlog.repetitivelearning.exception;

public class InvalidSortFieldException extends BusinessException {

  public InvalidSortFieldException(String requestSort) {
    super(ErrorCode.INVALID_SORT_FIELD, "지원하지 않는 정렬 조건입니다.");
  }
}
