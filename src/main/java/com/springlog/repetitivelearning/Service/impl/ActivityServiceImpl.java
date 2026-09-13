package com.springlog.repetitivelearning.Service.impl;

import com.springlog.repetitivelearning.Service.ActivityService;
import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.repository.ActivityRepository;
import com.springlog.repetitivelearning.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

  private final ActivityRepository activityRepository;
  private final UserRepository userRepository;

  @Override
  public List<ActivityResponse> activityByOwnerId(Long ownerId) {
    List<LearningActivity> activities = activityRepository.findByOwnerId(ownerId);

    return activities.stream()
        .map(ActivityResponse::from).toList();
  }
}
