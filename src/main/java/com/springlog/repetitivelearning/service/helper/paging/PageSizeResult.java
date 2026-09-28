package com.springlog.repetitivelearning.service.helper.paging;

public record PageSizeResult(

    int pageSize,
    String adjustedMessage

) {

  public static PageSizeResult of(int pageSize, String adjustedMessage) {
    return new PageSizeResult(pageSize, adjustedMessage);
  }

}
