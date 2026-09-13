package com.springlog.repetitivelearning.controller;

import com.springlog.repetitivelearning.Service.ActivityService;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
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
public class ActivityController {
  private final ActivityService activityService;

  @GetMapping
  public ResponseEntity<List<ActivityResponse>> getActivities(@RequestParam Long ownerId) {
    List<ActivityResponse> activities = activityService.activityByOwnerId(ownerId);

    return ResponseEntity.status(HttpStatus.OK).body(activities);
  }

}
