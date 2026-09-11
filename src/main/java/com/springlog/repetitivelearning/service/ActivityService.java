package com.springlog.repetitivelearning.service;

import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import java.util.List;

public interface ActivityService {

  List<ActivityResponse> getActivitiesByOwnerId(Long ownerId);

}
