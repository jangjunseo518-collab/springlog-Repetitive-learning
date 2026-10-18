package com.springlog.repetitivelearning.exception;

public class InvalidActivityException extends BusinessException {

  public InvalidActivityException(String detail) {
    super(ErrorCode.INVALID_ACTIVITY_INPUT, detail);
  }
}