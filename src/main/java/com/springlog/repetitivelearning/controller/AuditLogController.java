package com.springlog.repetitivelearning.controller;

import com.springlog.repetitivelearning.dto.response.PageAuditResponse;
import com.springlog.repetitivelearning.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

  private final AuditService  auditService;

  @GetMapping
  public ResponseEntity<PageAuditResponse>  getAuditLogs(Pageable pageable) {
    PageAuditResponse auditLogs = auditService.getAuditLogs(pageable);
    return ResponseEntity.status(HttpStatus.OK).body(auditLogs);
  }

}
