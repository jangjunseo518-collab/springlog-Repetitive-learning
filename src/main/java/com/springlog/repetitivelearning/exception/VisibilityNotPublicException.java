package com.springlog.repetitivelearning.exception;

import com.springlog.repetitivelearning.domain.type.Visibility;

public class VisibilityNotPublicException extends BusinessException {

  public VisibilityNotPublicException(Visibility visibility)
  {
    super(ErrorCode.VISIBILITY_NOT_PUBLIC, "공개 활동 조회로는 공개활동만 조회가 가능합니다.");
  }
}
