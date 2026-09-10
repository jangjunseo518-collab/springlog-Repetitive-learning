package com.springlog.repetitivelearning.controller;

import com.springlog.repetitivelearning.dto.response.ActivityResponseDto;
import com.springlog.repetitivelearning.service.LearningActivityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class LearningActivityController {

  private final LearningActivityService learningActivityService;

  @GetMapping
  public ResponseEntity <List<ActivityResponseDto>> getActivities(@RequestParam Long ownerId) {
    List<ActivityResponseDto> activities = learningActivityService.getLearningActivitiesByOwnerId(
        ownerId);

    return ResponseEntity.status(HttpStatus.OK).body(activities);
  }


}
