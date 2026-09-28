package com.springlog.repetitivelearning.service.helper.page;

import com.springlog.repetitivelearning.domain.type.Visibility;
import org.springframework.data.domain.Pageable;

public record PagingSetup(
    Visibility visibility,
    String adjustedSizeMessage,
    Pageable pageable
) {

  public static PagingSetup of(Visibility visibility, String adjustedSizeMessage,  Pageable pageable) {
    return new PagingSetup(visibility, adjustedSizeMessage, pageable);
  }
}
