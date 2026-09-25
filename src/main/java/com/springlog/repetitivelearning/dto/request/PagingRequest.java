package com.springlog.repetitivelearning.dto.request;

import com.springlog.repetitivelearning.domain.type.Visibility;
import org.springframework.data.domain.Sort;

public record PagingRequest(
    Visibility visibility,
    String sort,
    Sort.Direction direction,
    Integer page,
    Integer requestedSize
) {

}
