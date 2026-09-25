package com.springlog.repetitivelearning.service.helper.paging;

import com.springlog.repetitivelearning.domain.type.Visibility;
import org.springframework.data.domain.Pageable;

public record PagingSetup(
    Pageable pageable,
    Visibility visibility,
    String pageSizeMessage
) {
  public static PagingSetup of(Pageable pageable, Visibility visibility, String pageSizeMessage) {
   return new PagingSetup(pageable, visibility, pageSizeMessage);
  }
}
