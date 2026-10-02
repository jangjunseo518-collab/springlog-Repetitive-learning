package com.springlog.repetitivelearning.controller;

import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.request.AddTagsRequest;
import com.springlog.repetitivelearning.dto.request.CreateActivityRequest;
import com.springlog.repetitivelearning.dto.request.PagingRequest;
import com.springlog.repetitivelearning.dto.request.SearchRequest;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.dto.response.PageResponse;
import com.springlog.repetitivelearning.dto.response.SliceResponse;
import com.springlog.repetitivelearning.service.ActivityDashboard;
import com.springlog.repetitivelearning.service.ActivityService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
  private final ActivityDashboard dashboard;

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

  @GetMapping("/page")
  public ResponseEntity<PageResponse> getActivitiesByPage(
      @ModelAttribute PagingRequest pagingRequest) {

    PageResponse activitiesByPage = activityService.getAllPublicActivitiesByPage(
        pagingRequest);
    return ResponseEntity.status(HttpStatus.OK).body(activitiesByPage);
  }

  @GetMapping("/slice")
  public ResponseEntity<SliceResponse> getActivitiesBySlice(
      @ModelAttribute PagingRequest pagingRequest) {

    SliceResponse activitiesSlice = activityService.getAllPublicActivitiesBySlice(
        pagingRequest);

    return ResponseEntity.status(HttpStatus.OK).body(activitiesSlice);
  }

  @GetMapping("/search")
  public ResponseEntity<List<ActivityResponse>> getActivitiesBySearch(
      @ModelAttribute SearchRequest searchRequest) {
    List<ActivityResponse> activities = activityService.getAllPublicActivities(
        searchRequest);
    return ResponseEntity.status(HttpStatus.OK).body(activities);
  }

  @GetMapping("/all/tags")
  public ResponseEntity<List<ActivityResponse>> getAllTags(
      @RequestParam(required = false) String tag,
      @RequestParam Visibility visibility) {
    List<ActivityResponse> activityByTag = dashboard.findByTag(tag, visibility);
    return ResponseEntity.status(HttpStatus.OK).body(activityByTag);
  }

  @GetMapping("/group/category")
  public ResponseEntity<Map<ActivityCategory, List<ActivityResponse>>> getGroupByCategory(
      @RequestParam Visibility visibility
  ){
    Map<ActivityCategory, List<ActivityResponse>> categoryListMap = dashboard.groupByCategory(
        visibility);
    return ResponseEntity.status(HttpStatus.OK).body(categoryListMap);
  }

  @GetMapping("/count/category")
  public ResponseEntity<Map<ActivityCategory, Long>> getCountByCategory(
      @RequestParam Visibility visibility
  ) {
    Map<ActivityCategory, Long> activityCategoryLongMap = dashboard.countByCategroy(visibility);
    return ResponseEntity.status(HttpStatus.OK).body(activityCategoryLongMap);
  }

  @GetMapping("/sort/all/tags")
  public ResponseEntity<Set<String>>  getAllTags(
      @RequestParam Visibility visibility
  ) {
    Set<String> sortAllTags = dashboard.sortByAllTags(visibility);
    return ResponseEntity.status(HttpStatus.OK).body(sortAllTags);
  }
}
