package com.springlog.repetitivelearning.exception;

public class InvalidOwnerInputException extends BusinessException {

  public  InvalidOwnerInputException(String detail) {
    super(ErrorCode.INVALID_USER_INPUT, detail);
  }

}
