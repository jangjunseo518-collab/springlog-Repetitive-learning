package com.springlog.repetitivelearning.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageResponse(
    Page<ActivityResponse> data,
    String adjustedSizeMessage

) {
  public static PageResponse of(Page<ActivityResponse> data, String adjustedSizeMessage) {
    return new PageResponse(data,adjustedSizeMessage);
  }

}
