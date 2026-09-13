package com.springlog.repetitivelearning.Service;

import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import java.util.List;

public interface ActivityService {

  List<ActivityResponse> activityByOwnerId(Long ownerId);


}
