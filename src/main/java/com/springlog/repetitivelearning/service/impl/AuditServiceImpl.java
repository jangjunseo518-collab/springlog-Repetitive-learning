package com.springlog.repetitivelearning.service.impl;

import static com.springlog.repetitivelearning.service.helper.paging.PageSizeClamper.clampedPageSize;

import com.springlog.repetitivelearning.domain.ActivityAuditLog;
import com.springlog.repetitivelearning.dto.response.AuditLogResponse;
import com.springlog.repetitivelearning.dto.response.PageAuditResponse;
import com.springlog.repetitivelearning.exception.PageValidationFailureException;
import com.springlog.repetitivelearning.repository.AuditLogRepository;
import com.springlog.repetitivelearning.service.AuditService;
import com.springlog.repetitivelearning.service.helper.paging.PageSizeResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditServiceImpl implements AuditService {

  private final AuditLogRepository auditLogRepository;

  @Override
  public PageAuditResponse getAuditLogs(Pageable pageable) {
    PageSizeResult size = clampedPageSize(pageable.getPageSize());
    PageRequest safe = PageRequest.of(pageable.getPageNumber(), size.pageSize());

    Page<AuditLogResponse> data = auditLogRepository.findAllByOrderByIdDesc(safe)
        .map(AuditLogResponse::of);

    return new PageAuditResponse(data, size.adjustedMessage());
  }
}
