package com.springlog.repetitivelearning.exception;

public class OwnerNotFoundException extends BusinessException {

  public OwnerNotFoundException(Long ownerId) {
    super(ErrorCode.OWNER_NOT_FOUND, "사용자를 찾지 못 했습니다. ownerId: " + ownerId);
  }
}
