package com.springlog.repetitivelearning.service.helper.paging;

import com.springlog.repetitivelearning.domain.type.Visibility;
import org.springframework.data.domain.Pageable;

public record PagingSetup(
    Visibility visibility,
    String adjustedMessage,
    Pageable pageable

) {

  public static PagingSetup of(Visibility visibility, String adjustedMessage, Pageable pageable) {
    return new PagingSetup(visibility, adjustedMessage, pageable);
  }

}
