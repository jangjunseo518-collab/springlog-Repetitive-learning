package com.springlog.repetitivelearning.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PagingResponse(
    Page<ActivityResponse> data,
    String pageSizeMessage
) {

  public static PagingResponse of(Page<ActivityResponse> data, String pageSizeMessage) {
    return new PagingResponse(data, pageSizeMessage);
  }

}
