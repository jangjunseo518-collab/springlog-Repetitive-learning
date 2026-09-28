package com.springlog.repetitivelearning.service.impl;


import static com.springlog.repetitivelearning.service.helper.HelperMethod.publicVisibilityValidator;
import static com.springlog.repetitivelearning.service.helper.page.PagingHelper.buildPagingSetup;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.request.AddTagsRequest;
import com.springlog.repetitivelearning.dto.request.CreateActivityRequest;
import com.springlog.repetitivelearning.dto.request.PagingRequest;
import com.springlog.repetitivelearning.dto.request.SearchRequest;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.dto.response.PageResponse;
import com.springlog.repetitivelearning.dto.response.SliceResponse;
import com.springlog.repetitivelearning.exception.ActivityNotFoundException;
import com.springlog.repetitivelearning.exception.OwnerNotFoundException;
import com.springlog.repetitivelearning.repository.ActivityRepository;
import com.springlog.repetitivelearning.repository.UserRepository;
import com.springlog.repetitivelearning.service.ActivityService;
import com.springlog.repetitivelearning.service.helper.page.PagingSetup;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

  private final ActivityRepository activityRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public ActivityResponse createActivity(CreateActivityRequest request) {
    Long ownerId = request.ownerId();
    User owner = userRepository.findById(ownerId).orElseThrow(
        () -> new OwnerNotFoundException(ownerId)
    );

    LearningActivity activity = new LearningActivity(request.title(), request.minutes(),
        request.studiedOn(), request.visibility(), request.category(),
        request.instructorName(), request.completionRate(), request.bookTitle());
    activity.assignOwner(owner);
    LearningActivity saved = activityRepository.save(activity);

    return ActivityResponse.from(saved);
  }

  @Override
  public ActivityResponse getActivity(Long activityId) {

    LearningActivity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new ActivityNotFoundException(activityId)
    );

    return ActivityResponse.from(activity);
  }

  @Override
  public List<ActivityResponse> getActivitiesByOwnerId(Long ownerId) {
    List<LearningActivity> activities = activityRepository.findByOwnerId(ownerId);

    return activities.stream()
        .map(ActivityResponse::from).toList();
  }

  @Override
  @Transactional
  public ActivityResponse addTags(Long activityId, AddTagsRequest request) {
    LearningActivity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new ActivityNotFoundException(activityId));

    request.tags().forEach(activity::addTag);
    LearningActivity saved = activityRepository.save(activity);

    return ActivityResponse.from(saved);
  }

  @Override
  public boolean tagExistence(Long activityId, String tag) {

    LearningActivity activity = activityRepository.findById(activityId).orElseThrow(
        () -> new ActivityNotFoundException(activityId)
    );

    boolean hasTag = activity.hasTag(tag);

    return hasTag;
  }

  @Override
  @Transactional
  public void deleteTag(Long activityId, String tag) {

    LearningActivity activity = activityRepository.findById(activityId).orElseThrow(
        () -> new ActivityNotFoundException(activityId)
    );

    activity.removeTag(tag);
  }

  @Override
  public PageResponse getAllPublicActivitiesPage(PagingRequest pageRequest) {

    PagingSetup pagingSetup = buildPagingSetup(pageRequest);

    Visibility visibility = pagingSetup.visibility();
    Pageable pageable = pagingSetup.pageable();

    Page<LearningActivity> activities = activityRepository
                                       .findByVisibility(visibility, pageable);
    Page<ActivityResponse> activitiesBypage = activities.map(ActivityResponse::from);

    return PageResponse.of(activitiesBypage, pagingSetup.adjustedSizeMessage());
  }

  @Override
  public SliceResponse getAllPublicActivitiesSlice(PagingRequest pageRequest) {
    PagingSetup pagingSetup = buildPagingSetup(pageRequest);

    Visibility visibility = pagingSetup.visibility();
    Pageable pageable = pagingSetup.pageable();

    Slice<LearningActivity> activities = activityRepository
                               .findAllByVisibility(visibility, pageable);
    boolean hasNext = activities.hasNext();

    Slice<ActivityResponse> activitiesBySlice = activities.map(ActivityResponse::from);

    return SliceResponse.of(activitiesBySlice, pagingSetup.adjustedSizeMessage(), hasNext );
  }

  @Override
  public List<ActivityResponse> getSearchActivities(SearchRequest searchRequest) {

    Visibility visibility = publicVisibilityValidator(searchRequest.visibility());

    if(searchRequest.category() != null) {
      List<LearningActivity> allByCategoryAndVisibility = activityRepository.findAllByCategoryAndVisibility(
          searchRequest.category(), visibility);
      return allByCategoryAndVisibility.stream().map(ActivityResponse::from).toList();
    }
    if(searchRequest.titleKeyword() != null && !searchRequest.titleKeyword().isBlank()) {
      List<LearningActivity> byTitleAndVisibility = activityRepository.findByTitleContainingIgnoreCaseAndVisibility(
          searchRequest.titleKeyword(), visibility);
      return byTitleAndVisibility.stream().map(ActivityResponse::from).toList();
    }
    if (searchRequest.minMinutes() != null ) {
      List<LearningActivity> byMinutesGreaterThanEqualAndVisibility = activityRepository.findByMinutesGreaterThanEqualAndVisibility(
          searchRequest.minMinutes(), visibility);
      return byMinutesGreaterThanEqualAndVisibility.stream().map(ActivityResponse::from).toList();
    }

    List<LearningActivity> byVisibility = activityRepository.findByVisibility(visibility);
    return byVisibility.stream().map(ActivityResponse::from).toList();

  }
}
