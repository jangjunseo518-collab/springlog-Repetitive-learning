package com.springlog.repetitivelearning.dto.response;

import org.springframework.data.domain.Slice;

public record SliceResponse(
    Slice<ActivityResponse> data,
    String adjustedMessage,
    Boolean hasNext
) {

  public static SliceResponse of(Slice<ActivityResponse> data, String adjustedMessage, boolean hasNext) {
    return new SliceResponse(data, adjustedMessage, hasNext);
  }
}
