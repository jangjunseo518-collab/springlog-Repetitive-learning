package com.springlog.repetitivelearning.exception;

public class InvalidUserInputException extends BusinessException {

  public InvalidUserInputException(String detail) {
    super(ErrorCode.INVALID_USER_INPUT, detail);
  }
}
