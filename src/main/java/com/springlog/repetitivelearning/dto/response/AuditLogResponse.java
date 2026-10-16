package com.springlog.repetitivelearning.dto.response;

import com.springlog.repetitivelearning.domain.ActivityAuditLog;
import com.springlog.repetitivelearning.domain.type.ActionCategory;
import java.time.LocalDateTime;

public record AuditLogResponse(
    Long auditLogId,
    LocalDateTime createdAt,
    ActionCategory category,
    String detail,
    Long actor
) {

  public static AuditLogResponse of(ActivityAuditLog auditLog) {
  return   new AuditLogResponse(auditLog.getId(), auditLog.getCreatedAt()
    ,auditLog.getCategory(), auditLog.getDetails(), auditLog.getOwnerId());
  }

}
