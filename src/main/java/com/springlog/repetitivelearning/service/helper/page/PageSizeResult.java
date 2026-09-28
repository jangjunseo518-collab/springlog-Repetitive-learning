package com.springlog.repetitivelearning.service.helper.page;

import org.springframework.data.domain.PageRequest;

public record PageSizeResult(
    int size,
    String adjustedSizeMessage
) {

  public static PageSizeResult of(int size, String adjustedSizeMessage) {
    return new PageSizeResult(size, adjustedSizeMessage);
  }

}
