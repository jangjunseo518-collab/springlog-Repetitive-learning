package com.springlog.repetitivelearning.dto.response;

import org.springframework.data.domain.Page;

public record PageAuditResponse(
    Page<AuditLogResponse> data,
    String adjustMessage
) {

}
