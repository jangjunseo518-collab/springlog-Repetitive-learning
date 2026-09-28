package com.springlog.repetitivelearning.exception;

import com.springlog.repetitivelearning.domain.type.Visibility;

public class VisibilityNotPublicException extends RuntimeException {

  public VisibilityNotPublicException(Visibility visibility)
  {
    super("공개 활동 조회로는 공개활동만 조회가 가능합니다.");
  }
}
