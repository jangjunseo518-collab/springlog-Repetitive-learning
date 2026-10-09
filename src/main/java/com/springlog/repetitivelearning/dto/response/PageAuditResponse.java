package com.springlog.repetitivelearning.dto.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;

@JsonInclude(NON_NULL)
public record PageAuditResponse(
    Page<AuditLogResponse> data,
    String adjustedMessage
) {

}
