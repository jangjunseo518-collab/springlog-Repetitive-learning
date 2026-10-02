package com.springlog.repetitivelearning.dto.response;

import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import java.util.Map;

public record ActivityCountResponse(
    long total,
    Map<ActivityCategory, Long> categoryCount
) {
  public static ActivityCountResponse of(Long total,  Map<ActivityCategory, Long> categoryCount) {
    return new ActivityCountResponse(total, categoryCount);
  }
}
