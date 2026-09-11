package com.springlog.repetitivelearning.service.impl;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.repository.ActivityRepository;
import com.springlog.repetitivelearning.service.activityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AtivityServiceImpl implements activityService {

  private final ActivityRepository activityRepository;

  @Override
  public List<ActivityResponse> getActivitiesByOwnerId(Long ownerId) {
    List<LearningActivity> activities = activityRepository.findByOwnerId(ownerId);
   return activities.stream()
        .map(ActivityResponse::from).toList();
  }
}
