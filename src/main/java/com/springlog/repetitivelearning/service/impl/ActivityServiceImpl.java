package com.springlog.repetitivelearning.service.impl;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.dto.request.CreateActivityRequest;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.exception.ActivityNotFoundException;
import com.springlog.repetitivelearning.exception.OwnerNotFoundException;
import com.springlog.repetitivelearning.repository.ActivityRepository;
import com.springlog.repetitivelearning.repository.UserRepository;
import com.springlog.repetitivelearning.service.ActivityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

  private final ActivityRepository activityRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public ActivityResponse createActivity(CreateActivityRequest request) {
    Long ownerId = request.ownerId();
    User owner = userRepository.findById(ownerId)
        .orElseThrow(() -> new OwnerNotFoundException(ownerId));

    LearningActivity activity = new LearningActivity(request.title(), request.minutes(),
        request.studiedOn(), request.visibility(), request.category(),
        request.instructorName(), request.completionRate(), request.bookTitle());
    activity.assignOwner(owner);
    LearningActivity saved = activityRepository.save(activity);

    return ActivityResponse.from(saved);
  }

  @Override
  public ActivityResponse getActivityById(Long activityId) {
    LearningActivity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new ActivityNotFoundException(activityId));

    return ActivityResponse.from(activity);
  }

  @Override
  public List<ActivityResponse> getActivitiesByOwnerId(Long ownerId) {
    List<LearningActivity> activities = activityRepository.findByOwnerId(ownerId);

    return activities.stream()
        .map(ActivityResponse::from).toList();
  }
}
