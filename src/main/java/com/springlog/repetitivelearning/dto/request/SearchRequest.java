package com.springlog.repetitivelearning.dto.request;

import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;

public record SearchRequest(
 ActivityCategory category,
 String keyword,
 Integer minMinutes,
 Visibility visibility
) {

}
