package com.springlog.repetitivelearning.dto.response;

import com.springlog.repetitivelearning.domain.ActivityAuditLog;
import com.springlog.repetitivelearning.domain.type.ActionCategory;
import java.time.LocalDateTime;

public record AuditLogResponse(
    ActionCategory category,
    Long id,
    String detail,
    String owner,
    LocalDateTime createdAt
) {

  public static AuditLogResponse of(ActivityAuditLog auditLog) {
    return new AuditLogResponse(auditLog.getActionCategory(), auditLog.getId(),
    auditLog.getDetail(), auditLog.getOwnerId(), auditLog.getCreatedAt());
  }

}
