package com.springlog.repetitivelearning.service.helper;

import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.exception.VisibilityNotPublicException;

public final class HelperMethod {

  private HelperMethod() {
  }

  //visibility 정규화 헬퍼
  public static Visibility visibilityPublicValidator(Visibility visibility) {
    if(visibility == null) {
      visibility = Visibility.PUBLIC;
    } else if( visibility == Visibility.PRIVATE ) {
      throw new VisibilityNotPublicException(visibility);
    }
    return visibility;
  }

}
