package com.springlog.repetitivelearning.dto.response;

import org.springframework.data.domain.Slice;

public record SliceResponse(
    Slice<ActivityResponse> data,
    String adjustedSizeMessage,
    boolean hasNext
) {

  public static SliceResponse of(Slice<ActivityResponse> data,
                                    String adjustedSizeMessage,  boolean hasNext) {
    return new SliceResponse(data, adjustedSizeMessage, hasNext);
  }

}
