package com.springlog.repetitivelearning.controller;

import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.request.AddTagRequest;
import com.springlog.repetitivelearning.dto.request.CreateActivityRequest;
import com.springlog.repetitivelearning.dto.request.PagingRequest;
import com.springlog.repetitivelearning.dto.request.SearchRequest;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.dto.response.PagingResponse;
import com.springlog.repetitivelearning.dto.response.SliceResponse;
import com.springlog.repetitivelearning.service.ActivityDashboard;
import com.springlog.repetitivelearning.service.ActivityService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
  private final ActivityDashboard activityDashboard;

  @PostMapping
  public ResponseEntity<ActivityResponse> createActivity(
      @RequestBody @Valid CreateActivityRequest request){
    ActivityResponse activity = activityService.createActivity(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(activity);
  }

  @PostMapping("/{activityId}/tags")
  public ResponseEntity<ActivityResponse> addTag(@PathVariable Long activityId , @Valid @RequestBody AddTagRequest tags){
    ActivityResponse activityAddedTag = activityService.addTags(activityId, tags);

    return ResponseEntity.status(HttpStatus.OK).body(activityAddedTag);
  }

  @GetMapping("/{activityId}/tags/{tag}")
  public ResponseEntity<Boolean> tagExistence(@PathVariable Long activityId , @PathVariable String tag){
    boolean existence = activityService.tagExistence(activityId, tag);

    return ResponseEntity.status(HttpStatus.OK).body(existence);
  }

  @DeleteMapping("/{activityId}/tags/{tag}")
  public ResponseEntity<Void> deleteTag(@PathVariable Long activityId , @PathVariable String tag){
    activityService.deleteTag(activityId, tag);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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

  @GetMapping("/page")
  public ResponseEntity<PagingResponse>  getActivitiesByPage(@ModelAttribute PagingRequest request) {
    PagingResponse allActivities = activityService.getAllActivities(request);
    return ResponseEntity.status(HttpStatus.OK).body(allActivities);
  }

  @GetMapping("/slice")
  public ResponseEntity<SliceResponse> getActivitiesBySlice(@ModelAttribute PagingRequest request) {
    SliceResponse allActivitiesSlice = activityService.getAllActivitiesSlice(request);
    return ResponseEntity.status(HttpStatus.OK).body(allActivitiesSlice);
  }

  @GetMapping("/search")
  public ResponseEntity<List<ActivityResponse>> getActivitiesBySearch(@ModelAttribute SearchRequest request){

    List<ActivityResponse> searchActivities = activityService.getSearchActivities(request);
    return ResponseEntity.status(HttpStatus.OK).body(searchActivities);
  }

  @GetMapping("/tags")
  public ResponseEntity<List<ActivityResponse>> getActivitiesByTag(
      @RequestParam String tag,
      @RequestParam (required = false)Visibility visibility){

    List<ActivityResponse> activitiesByTag = activityDashboard.findByTag(tag, visibility);

    return ResponseEntity.status(HttpStatus.OK).body(activitiesByTag);

  }

  @GetMapping("/group")
  public ResponseEntity<Map<ActivityCategory, List<ActivityResponse>>> getActivitiesByGroup(
      @RequestParam(required = false) Visibility visibility
  ) {
    Map<ActivityCategory, List<ActivityResponse>> categoryGroup
        = activityDashboard.groupByCategory(visibility);

    return ResponseEntity.status(HttpStatus.OK).body(categoryGroup);
  }

  @GetMapping("/categories")
  public ResponseEntity<Map<ActivityCategory, Long>> getCategories(
      @RequestParam(required = false) Visibility visibility
  ) {
    Map<ActivityCategory, Long> countByCategory = activityDashboard.countByCategory(
        visibility);

    return ResponseEntity.status(HttpStatus.OK).body(countByCategory);
  }

  @GetMapping("/tags/all")
  public ResponseEntity<Set<String>>  getAllTags(
      @RequestParam(required = false) Visibility visibility
  ) {
    Set<String> sortByTags = activityDashboard.getSortByTags(visibility);

    return ResponseEntity.status(HttpStatus.OK).body(sortByTags);
  }



}
