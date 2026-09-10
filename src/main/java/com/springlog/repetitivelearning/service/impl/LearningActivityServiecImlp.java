package com.springlog.repetitivelearning.service.impl;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.dto.response.ActivityResponseDto;
import com.springlog.repetitivelearning.repository.LearninActivityRepository;
import com.springlog.repetitivelearning.service.LearningActivityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LearningActivityServiecImlp implements LearningActivityService {

  private final LearninActivityRepository Repository;


  @Override
  @Transactional(readOnly = true)
  public List<ActivityResponseDto> getLearningActivitiesByOwnerId(Long ownerId) {

    List<LearningActivity> activities = Repository.findByOwnerId(ownerId);
     return activities.stream()
         .map(ActivityResponseDto::from)
         .toList();
  }
}
