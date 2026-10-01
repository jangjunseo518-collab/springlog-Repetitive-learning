package com.springlog.repetitivelearning.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Slice;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SliceResponse(
    Slice<ActivityResponse> data,
    String adjustedMessage,
    boolean hasNext
) {

  public static SliceResponse of(Slice<ActivityResponse> slice,
                      String adjustedMessage, boolean hasNext) {
    return new SliceResponse(slice, adjustedMessage, hasNext);
  }

}
