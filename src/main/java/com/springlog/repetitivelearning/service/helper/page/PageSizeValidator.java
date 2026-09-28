package com.springlog.repetitivelearning.service.helper.page;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageSizeValidator {

  private static final int MAX_PAGE_SIZE = 100;
  private static final int MIN_PAGE_SIZE = 1;
  private static final int DEFAULT_VALUE = 5;

  public static PageSizeResult resolvePageSize(Integer requestedSize) {

    if (requestedSize == null) {
      return PageSizeResult.of(DEFAULT_VALUE, null);
    }

    if (requestedSize < MIN_PAGE_SIZE) {
      return PageSizeResult.of(
          MIN_PAGE_SIZE,
          "페이지는 최소 1 이상을 입력해주세요. page: " + requestedSize);
    }

    if (requestedSize > MAX_PAGE_SIZE) {
      return PageSizeResult.of(
          MAX_PAGE_SIZE,
          "페이지는 최대 100까지만 입력 가능합니다. page: " + requestedSize);
    }

    return PageSizeResult.of(requestedSize,  null);

  }

}
