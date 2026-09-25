package com.springlog.repetitivelearning.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Slice;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SliceResponse(

    Slice<ActivityResponse> data,
    String pageSizeMessage,
    boolean hasNext

) {

  public static SliceResponse of(Slice<ActivityResponse> data, String pageSizeMessage,  boolean hasNext) {
    return new  SliceResponse(data, pageSizeMessage,  hasNext);
  }

}
