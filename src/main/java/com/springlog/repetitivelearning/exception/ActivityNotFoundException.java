package com.springlog.repetitivelearning.exception;

public class ActivityNotFoundException extends RuntimeException {

  public ActivityNotFoundException(Long id) {
    super("활동을 찾을 수 없습니다. id: " + id);
  }
}
