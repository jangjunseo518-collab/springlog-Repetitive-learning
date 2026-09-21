package com.springlog.repetitivelearning.service;

import com.springlog.repetitivelearning.dto.request.CreateActivityRequest;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import java.util.List;

public interface ActivityService {

  ActivityResponse createActivity(CreateActivityRequest request);

  ActivityResponse getActivity(Long activityId);

  List<ActivityResponse> getActivitiesByOwnerId(Long ownerId);

}
