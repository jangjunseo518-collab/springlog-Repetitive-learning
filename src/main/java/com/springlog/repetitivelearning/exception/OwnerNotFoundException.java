package com.springlog.repetitivelearning.exception;

public class OwnerNotFoundException extends RuntimeException {

  public OwnerNotFoundException(Long ownerId) {
    super("사용자를 찾지 못 했습니다. ownerId: " + ownerId);
  }
}
