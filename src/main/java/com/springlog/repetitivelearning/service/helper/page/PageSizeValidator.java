package com.springlog.repetitivelearning.service.helper.page;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageSizeValidator {

  private static final int PAGE_DEFAULT_SIZE = 5;
  public static final int PAGE_MIN_SIZE = 1;
  public static final int PAGE_MAX_SIZE = 100;

  public static PageSizeResult pageClamp(Integer pageSize) {

    if(pageSize == null ) {
      return PageSizeResult.of(PAGE_DEFAULT_SIZE,null);
    }
    if(pageSize < PAGE_MIN_SIZE) {
      return PageSizeResult.of(PAGE_MIN_SIZE,
          "pageSize는 1 이상 부터 입력 가능합니다. \n입력 값:" + pageSize
      +"| 조정 값: "+PAGE_MIN_SIZE);
    }
    if(pageSize > PAGE_MAX_SIZE) {
      return PageSizeResult.of(PAGE_MIN_SIZE,
          "pageSize는 100까지 입력 가능합니다. \n입력 값:" + pageSize
          +"| 조정 값: "+PAGE_MAX_SIZE);
    }

    return PageSizeResult.of(pageSize,null);

  }


}
