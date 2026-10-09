package com.springlog.repetitivelearning.repository;

import com.springlog.repetitivelearning.domain.ActivityAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<ActivityAuditLog, Long> {

  Page<ActivityAuditLog> findAllByOrderByIdDesc(Pageable pageable);

}
