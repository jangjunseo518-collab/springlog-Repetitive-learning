package com.springlog.repetitivelearning.dto.response;

import com.springlog.repetitivelearning.domain.ActivityAuditLog;
import java.time.LocalDateTime;

public record AuditLogResponse(
    Long id,
    String detail,
    String owner,
    LocalDateTime createAt
) {

  public static AuditLogResponse of(ActivityAuditLog auditLog) {
    return new AuditLogResponse(auditLog.getId(), auditLog.getDetail()
    , auditLog.getOwnerId(), auditLog.getCreatedAt());
  }

}
