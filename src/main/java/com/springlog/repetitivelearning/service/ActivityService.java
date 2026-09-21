package com.springlog.repetitivelearning.service;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.dto.request.CreateActivityRequest;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import java.util.List;

public interface ActivityService {

  ActivityResponse createActivity(CreateActivityRequest result);
  List<ActivityResponse> getActivitiesByOwnerId(Long ownerId);
  ActivityResponse getActivityById(Long activityId);
}
