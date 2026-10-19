package com.springlog.repetitivelearning.exception;

public class InvalidTagException extends BusinessException{

  public InvalidTagException(String detail) {
        super(ErrorCode.INVALID_TAG, detail);
  }

}
