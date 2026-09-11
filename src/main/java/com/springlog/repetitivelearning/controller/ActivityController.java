package com.springlog.repetitivelearning.controller;

import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.service.activityService;
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

  private final activityService activityService;

  @GetMapping
  public ResponseEntity<List<ActivityResponse>> getActivitiesByOwnerId(@RequestParam Long ownerId) {
    List<ActivityResponse> activities = activityService.getActivitiesByOwnerId(ownerId);

    return ResponseEntity.status(HttpStatus.OK).body(activities);
  }

}
