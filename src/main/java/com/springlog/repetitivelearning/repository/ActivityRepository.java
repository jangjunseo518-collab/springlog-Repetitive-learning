package com.springlog.repetitivelearning.repository;

import com.springlog.repetitivelearning.domain.LearningActivity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<LearningActivity, Long> {

  List<LearningActivity> findByOwnerId(Long ownerId);
}
