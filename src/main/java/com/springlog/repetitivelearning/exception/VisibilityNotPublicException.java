package com.springlog.repetitivelearning.exception;

import com.springlog.repetitivelearning.domain.type.Visibility;

public class VisibilityNotPublicException extends RuntimeException {

  public VisibilityNotPublicException(Visibility visibility) {
    super("공개 활동 페이지 조회는 비공개 활동을 조회 할 수 없습니다. visibility: " + visibility);
  }
}
