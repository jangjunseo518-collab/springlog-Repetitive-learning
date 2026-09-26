package com.springlog.repetitivelearning.controller;

import com.springlog.repetitivelearning.dto.request.AddTagsRequest;
import com.springlog.repetitivelearning.dto.request.CreateActivityRequest;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.service.ActivityService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

  private final ActivityService activityService;

  @PostMapping
  public ResponseEntity<ActivityResponse> createActivity(
      @RequestBody @Valid CreateActivityRequest request){
    ActivityResponse activity = activityService.createActivity(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(activity);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ActivityResponse> getActivityById(@PathVariable Long id){
    ActivityResponse activity = activityService.getActivity(id);
    return ResponseEntity.status(HttpStatus.OK).body(activity);
  }

  @GetMapping
  public ResponseEntity<List<ActivityResponse>> getActivitiesByOwnerId(@RequestParam Long ownerId) {
    List<ActivityResponse> activities = activityService.getActivitiesByOwnerId(ownerId);

    return ResponseEntity.status(HttpStatus.OK).body(activities);
  }

  //tags
  @PostMapping("/{activityId}/tags")
  public ResponseEntity<ActivityResponse> addTags(
      @PathVariable Long activityId , @RequestBody @Valid AddTagsRequest request){
    ActivityResponse activity = activityService.addTags(activityId, request);

    return ResponseEntity.status(HttpStatus.OK).body(activity);
  }


  @GetMapping("/{activityId}/tags/{tag}")
  public ResponseEntity<Boolean> existenceTag(
      @PathVariable Long activityId ,
      @PathVariable String tag){
    boolean existenceTag = activityService.tagExistence(activityId, tag);

    return ResponseEntity.status(HttpStatus.OK).body(existenceTag);
  }

  @DeleteMapping("/{activityId}/tags/{tag}")
  public ResponseEntity<Void> deleteTag(
      @PathVariable Long activityId , @PathVariable String tag
  ) {
    activityService.deleteTag(activityId, tag);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
