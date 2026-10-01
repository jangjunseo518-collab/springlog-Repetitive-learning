package com.springlog.repetitivelearning.service.helper.page;

import com.springlog.repetitivelearning.domain.type.Visibility;
import org.springframework.data.domain.Pageable;

public record PagingSetup(

    Visibility visibility,
    PageSizeResult pageSizeResult,
    Pageable pageable

) {

  public static PagingSetup of(Visibility visibility,
      PageSizeResult pageSizeResult, Pageable pageable) {
    return new PagingSetup(visibility, pageSizeResult, pageable);
  }

}
