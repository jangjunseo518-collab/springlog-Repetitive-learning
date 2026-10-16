package com.springlog.repetitivelearning.service;

import com.springlog.repetitivelearning.dto.response.PageAuditResponse;
import org.springframework.data.domain.Pageable;

public interface AuditService {

  PageAuditResponse getAuditLogs(Pageable pageable);

}
