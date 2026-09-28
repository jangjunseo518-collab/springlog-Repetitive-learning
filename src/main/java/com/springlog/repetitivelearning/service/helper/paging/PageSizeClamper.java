package com.springlog.repetitivelearning.service.helper.paging;

import com.springlog.repetitivelearning.dto.request.PagingRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageSizeClamper {

  private final static int DEFAULT_PAGE_SIZE = 5;
  private  final static int MAX_PAGE_SIZE = 100;
  private  final static int MIN_PAGE_SIZE = 1;

  public static PageSizeResult clampedPageSize(Integer pageSize) {
    String adjustedMessage = null;
    int pagSize = pageSize == null? DEFAULT_PAGE_SIZE : pageSize;

    if(pagSize < MIN_PAGE_SIZE) {
      pagSize = MIN_PAGE_SIZE;
      adjustedMessage = "pageSize는 1 이상만 입력이 가능합니다 pageSize:  "+ pagSize;
    }

    if(pagSize > MAX_PAGE_SIZE) {
      pagSize = MAX_PAGE_SIZE;
      adjustedMessage = "pageSize는 100 이하만 입력이 가능합니다 pageSize: "+ pagSize ;
    }

    return PageSizeResult.of(pagSize, adjustedMessage);
  }

}
