package com.springlog.repetitivelearning.service.helper;

import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.exception.NotPublicVisibilityException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisibiltyValidator {

  public static Visibility publicVisibilityValidator(Visibility visibility) {

    if(visibility == null) {
      return Visibility.PUBLIC;
    }
    if(visibility != Visibility.PUBLIC) {
      throw new NotPublicVisibilityException(visibility);
    }
    return visibility;
  }



}
