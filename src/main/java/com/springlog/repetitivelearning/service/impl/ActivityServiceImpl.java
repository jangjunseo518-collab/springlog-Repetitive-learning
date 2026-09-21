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
  public ActivityResponse createActivity(CreateActivityRequest result) {
    Long ownerId = result.ownerId();
    User owner = userRepository.findById(ownerId)
        .orElseThrow(() -> new OwnerNotFoundException(ownerId));

    LearningActivity activity = new LearningActivity(result.title(), result.minutes(),
        result.studiedOn(), result.visibility(),
        result.category(), result.instructorName(),
        result.completionRate(), result.bookTitle());
    activity.assignOwner(owner);
    LearningActivity saved = activityRepository.save(activity);

    return ActivityResponse.from(saved);
  }

  @Override
  public List<ActivityResponse> getActivitiesByOwnerId(Long ownerId) {
    List<LearningActivity> activities = activityRepository.findByOwnerId(ownerId);

    return activities.stream()
        .map(ActivityResponse::from).toList();
  }

  @Override
  public ActivityResponse getActivityById(Long activityId) {
    LearningActivity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new ActivityNotFoundException(activityId));
    return  ActivityResponse.from(activity);
  }




}
