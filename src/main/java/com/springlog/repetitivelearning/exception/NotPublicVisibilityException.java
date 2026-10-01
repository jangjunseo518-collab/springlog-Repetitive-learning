package com.springlog.repetitivelearning.exception;

import com.springlog.repetitivelearning.domain.type.Visibility;

public class NotPublicVisibilityException extends RuntimeException {

  public NotPublicVisibilityException(Visibility visibility) {
    super("공개활동 조회로는 공개활동 외의 활동을 조회할 수 없습니다.");
  }
}
