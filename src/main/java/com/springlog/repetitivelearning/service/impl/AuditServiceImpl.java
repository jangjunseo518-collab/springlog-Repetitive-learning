package com.springlog.repetitivelearning.service.impl;

import static com.springlog.repetitivelearning.service.helper.paging.PageSizeClamper.clampedPageSize;

import com.springlog.repetitivelearning.domain.ActivityAuditLog;
import com.springlog.repetitivelearning.dto.response.AuditLogResponse;
import com.springlog.repetitivelearning.dto.response.PageAuditResponse;
import com.springlog.repetitivelearning.repository.AuditLogRepository;
import com.springlog.repetitivelearning.service.AuditService;
import com.springlog.repetitivelearning.service.helper.paging.PageSizeResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

  private final AuditLogRepository auditLogRepository;

  @Override
  public PageAuditResponse getAuditLogs(Pageable pageable) {
    PageSizeResult pageSizeResult = clampedPageSize(pageable.getPageSize());
    PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageSizeResult.pageSize());

    Page<AuditLogResponse> data = auditLogRepository.findAllByOrderByIdDesc(
        pageRequest).map(AuditLogResponse::of);
    return new PageAuditResponse(data, pageSizeResult.adjustedMessage());
  }
}
