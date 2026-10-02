package com.springlog.repetitivelearning.dto.response;

import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import java.util.Map;

public record CountCategoryResponse(
    long total,
    Map<ActivityCategory, Long> countByCategory
) {

  public static CountCategoryResponse of(long total, Map<ActivityCategory, Long> countByCategory) {

    return new CountCategoryResponse(total, countByCategory);
  }

}
