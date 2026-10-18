package com.springlog.repetitivelearning.exception;

public class InvalidFileException extends BusinessException {

  public InvalidFileException(String detail) {
    super(ErrorCode.INVALID_FILE, detail);
  }
}
