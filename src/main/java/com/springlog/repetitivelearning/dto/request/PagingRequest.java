package com.springlog.repetitivelearning.dto.request;

import com.springlog.repetitivelearning.domain.type.Visibility;
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Sort;

public record PagingRequest(
    Visibility visibility,
    String sort,
    Sort.Direction sortDirection,
    Integer page,
    Integer pageSize

) {

}
