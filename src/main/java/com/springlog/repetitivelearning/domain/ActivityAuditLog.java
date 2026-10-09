package com.springlog.repetitivelearning.domain;

import com.springlog.repetitivelearning.domain.type.ActionCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "activity_audit_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ActivityAuditLog extends BasicEntity {

  @Enumerated(EnumType.STRING)
  private ActionCategory actionCategory;
  @Column(length = 1000)
  private String detail;
  @Column(length = 50)
  private String ownerId;


}
