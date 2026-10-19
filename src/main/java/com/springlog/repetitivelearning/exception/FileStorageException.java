package com.springlog.repetitivelearning.exception;

public class FileStorageException extends BusinessException {

  public FileStorageException(String message) {
    super(ErrorCode.FILE_STORAGE_FAILED, message);
  }

  public FileStorageException(String message, Throwable cause) {
    super(ErrorCode.FILE_STORAGE_FAILED, message, cause);
  }
}
