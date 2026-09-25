package com.springlog.repetitivelearning.service.helper.paging;

public class PageSizeValidator {

  private static final int MIN_SIZE = 1;
  private static final int MAX_SIZE = 100;



  public static PageSizeResult resolvePageSize(int requestedPageSize) {
     int size = requestedPageSize;
     String message = null;

    if(requestedPageSize < MIN_SIZE) {
      size = MIN_SIZE;
      message = "페이지는 최소 1 이상이여야 합니다. page: " + requestedPageSize;
    }

    if(requestedPageSize > MAX_SIZE) {
      size = MAX_SIZE;
      message = "페이지는 최대 100 까지만 입력 가능합니다. page: " + requestedPageSize;
    }
  return PageSizeResult.of(size, message);

  }


}
