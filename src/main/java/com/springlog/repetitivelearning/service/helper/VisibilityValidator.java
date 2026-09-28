package com.springlog.repetitivelearning.service.helper;

import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.exception.VisibilityNotPublicException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisibilityValidator {

  public static Visibility visibilityPublicValidator(Visibility visibility) {
    if (visibility == null) {
      return Visibility.PUBLIC;
    }
    if (visibility != Visibility.PUBLIC) {
      throw new VisibilityNotPublicException(visibility);
    }
    return visibility;
  }



}
