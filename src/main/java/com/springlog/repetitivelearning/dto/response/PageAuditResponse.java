package com.springlog.repetitivelearning.dto.response;

import org.springframework.data.domain.Page;

public record PageAuditResponse(

    Page<AuditLogResponse> data,
    String adjustedMessage

) { // 필드가 두개 분이니 별도로 정적펙터리는 만들지 않는다.

}
