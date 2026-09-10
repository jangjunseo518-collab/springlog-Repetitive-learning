package com.springlog.repetitivelearning.service;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.dto.response.ActivityResponseDto;
import java.util.List;
import org.springframework.stereotype.Service;

public interface LearningActivityService {
 List<ActivityResponseDto> getLearningActivitiesByOwnerId(Long ownerId);
}
