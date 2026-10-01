package com.springlog.repetitivelearning.service.impl;

import static com.springlog.repetitivelearning.service.helper.VisibiltyValidator.publicVisibilityValidator;
import static com.springlog.repetitivelearning.service.helper.page.PagingHelper.buildPagingSetup;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.Visibility;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
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
  public PageResponse getActivitiesPage(PagingRequest pagingRequest) {
    PagingSetup pagingSetup = buildPagingSetup(pagingRequest);

    Visibility visibility = pagingSetup.visibility();
    Pageable pageable = pagingSetup.pageable();
    String adjustedMessage = pagingSetup.pageSizeResult().adjustedMessage();

    Page<LearningActivity> activities = activityRepository.findByVisibility(visibility, pageable);
    Page<ActivityResponse> activityPage = activities.map(ActivityResponse::from);

    return PageResponse.of(activityPage, adjustedMessage);
  }

  @Override
  public SliceResponse getActivitiesSlice(PagingRequest pagingRequest) {

    PagingSetup pagingSetup = buildPagingSetup(pagingRequest);
    Visibility visibility = pagingSetup.visibility();
    Pageable pageable = pagingSetup.pageable();
    String adjustedMessage = pagingSetup.pageSizeResult().adjustedMessage();

    Slice<LearningActivity> activities = activityRepository.findAllByVisibility(visibility,
        pageable);
    Slice<ActivityResponse> activitiesSlice = activities.map(ActivityResponse::from);
    boolean hasNext = activitiesSlice.hasNext();

    return SliceResponse.of(activitiesSlice, adjustedMessage, hasNext);
  }

  @Override
  public List<ActivityResponse> getAllActivitiesList(SearchRequest searchRequest) {
    Visibility visibility = publicVisibilityValidator(searchRequest.visibility());

    if(searchRequest.category() != null ) {
      List<LearningActivity> activities =
          activityRepository.findByCategoryAndVisibility(
          searchRequest.category(), visibility);
      List<ActivityResponse> activitiseList = activities.stream()
          .map(ActivityResponse::from).toList();
      return activitiseList;
    }
    if(searchRequest.titleKeyword() != null && !searchRequest.titleKeyword().isBlank()) {
      List<LearningActivity> activities =
          activityRepository.findByTitleContainingIgnoreCaseAndVisibility(
          searchRequest.titleKeyword(), visibility);
      return activities.stream()
          .map(ActivityResponse::from).toList();
    }
    if(searchRequest.minMinutes() != null) {
      List<ActivityResponse> activitiesList = activityRepository.findByMinutesGreaterThanEqualAndVisibility(
              searchRequest.minMinutes(), visibility).stream()
          .map(ActivityResponse::from).toList();
      return activitiesList;
    }

    return activityRepository.findByVisibility(visibility)
        .stream().map(ActivityResponse::from).toList();
  }
}
