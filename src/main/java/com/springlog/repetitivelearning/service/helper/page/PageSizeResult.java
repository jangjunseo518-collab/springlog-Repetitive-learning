package com.springlog.repetitivelearning.service.helper.page;

public record PageSizeResult(
    int pageSize,
    String adjustedMessage
) {

  public static PageSizeResult of(int pageSize, String adjustedMessage) {
    return new PageSizeResult(pageSize, adjustedMessage);
  }

}
