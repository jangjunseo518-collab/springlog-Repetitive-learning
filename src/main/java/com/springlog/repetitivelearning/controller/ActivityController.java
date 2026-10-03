package com.springlog.repetitivelearning.controller;

import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.request.AddTagsRequest;
import com.springlog.repetitivelearning.dto.request.ChangeTitleRequest;
import com.springlog.repetitivelearning.dto.request.CreateActivityRequest;
import com.springlog.repetitivelearning.dto.request.IncreaseMinutesRequest;
import com.springlog.repetitivelearning.dto.request.PagingRequest;
import com.springlog.repetitivelearning.dto.request.SearchRequest;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.dto.response.CountCategoryResponse;
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
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
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

  //Paging
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
      @RequestParam String tag,
      @RequestParam(required = false) Visibility visibility) {

    List<ActivityResponse> activityByTag = dashboard.findByTag(tag, visibility);

    return ResponseEntity.status(HttpStatus.OK).body(activityByTag);
  }

  @GetMapping("/group/category")
  public ResponseEntity<Map<ActivityCategory, List<ActivityResponse>>> getGroupByCategory(
      @RequestParam(required = false) Visibility visibility
  ){
    Map<ActivityCategory, List<ActivityResponse>> categoryListMap =
        dashboard.groupByCategory(visibility);

    return ResponseEntity.status(HttpStatus.OK).body(categoryListMap);
  }

  @GetMapping("/count/category")
  public ResponseEntity<CountCategoryResponse> getCountByCategory(
      @RequestParam(required = false) Visibility visibility
  ) {
    CountCategoryResponse countCategoryResponse = dashboard.countByCategory(visibility);

    return ResponseEntity.status(HttpStatus.OK).body(countCategoryResponse);
  }

  @GetMapping("/sort/all/tags")
  public ResponseEntity<Set<String>>  getAllTags(
      @RequestParam(required = false) Visibility visibility
  ) {
    Set<String> sortAllTags = dashboard.sortByAllTags(visibility);
    return ResponseEntity.status(HttpStatus.OK).body(sortAllTags);
  }

  //change
  @PatchMapping("/{activityId}/title")
  public ResponseEntity<ActivityResponse> changeTitle(@PathVariable Long activityId,
                           @RequestBody @Valid ChangeTitleRequest changeTitleRequest){
    ActivityResponse activityResponse = activityService.changeTitle(activityId, changeTitleRequest);
    return ResponseEntity.status(HttpStatus.OK).body(activityResponse);
  }

  @PatchMapping("/{activityId}/public")
  public ResponseEntity<ActivityResponse> changePublic(@PathVariable Long activityId){
    ActivityResponse activityResponse = activityService.changeToPublic(activityId);

    return ResponseEntity.status(HttpStatus.OK).body(activityResponse);
  }

  @PatchMapping("/{activityId}/private")
  public ResponseEntity<ActivityResponse> changePrivate(@PathVariable Long activityId){
    ActivityResponse activityResponse = activityService.changeToPrivate(activityId);

    return ResponseEntity.status(HttpStatus.OK).body(activityResponse);
  }

  @PostMapping("/{activityId}/minutes")
  public ResponseEntity<ActivityResponse> increaseMinutes(@PathVariable Long activityId,
                                     @RequestBody @Valid IncreaseMinutesRequest minutes) {
    ActivityResponse activityResponse = activityService.increaseMinutes(activityId, minutes);

    return ResponseEntity.status(HttpStatus.OK).body(activityResponse);
  }

  @DeleteMapping("/{activityId}")
  public ResponseEntity<Void> deleteActivity(@PathVariable Long activityId) {
    activityService.deleteActivity(activityId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
