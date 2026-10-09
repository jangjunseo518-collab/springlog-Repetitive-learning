package com.springlog.repetitivelearning.dto.response;

import com.springlog.repetitivelearning.domain.ActivityAuditLog;
import com.springlog.repetitivelearning.domain.type.ActionCategory;
import java.time.LocalDateTime;

public record AuditLogResponse(
   Long id,
   ActionCategory action,
   String  detail,
   String actor,
   LocalDateTime at
) {

  public static AuditLogResponse of(ActivityAuditLog activityAuditLog) {
   return new AuditLogResponse(activityAuditLog.getId(), activityAuditLog.getAction(),
        activityAuditLog.getDetail(), activityAuditLog.getActor(),
        activityAuditLog.getCreatedAt());

  }
}
