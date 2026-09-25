package com.springlog.repetitivelearning.service.impl;

import static com.springlog.repetitivelearning.service.helper.HelperMethod.visibilityPublicValidator;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.request.AddTagRequest;
import com.springlog.repetitivelearning.dto.request.CreateActivityRequest;
import com.springlog.repetitivelearning.dto.request.PagingRequest;
import com.springlog.repetitivelearning.dto.request.SearchRequest;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.dto.response.PagingResponse;
import com.springlog.repetitivelearning.dto.response.SliceResponse;
import com.springlog.repetitivelearning.exception.ActivityNotFoundException;
import com.springlog.repetitivelearning.exception.OwnerNotFoundException;
import com.springlog.repetitivelearning.exception.PageValidateFailureException;
import com.springlog.repetitivelearning.exception.PagingRequestException;
import com.springlog.repetitivelearning.repository.ActivityRepository;
import com.springlog.repetitivelearning.repository.UserRepository;
import com.springlog.repetitivelearning.service.ActivityService;
import com.springlog.repetitivelearning.service.helper.paging.PageSizeResult;
import com.springlog.repetitivelearning.service.helper.paging.PageSizeValidator;
import com.springlog.repetitivelearning.service.helper.paging.PagingSetup;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
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
  public PagingResponse getAllActivities(PagingRequest request) {

    PagingSetup pagingSetup = buildPagingSetup(request);

    Page<LearningActivity> activities =
        activityRepository.findByVisibility(pagingSetup.visibility(), pagingSetup.pageable());

    Page<ActivityResponse> activityPage = activities.map(ActivityResponse::from);
    PagingResponse pagingResponse = PagingResponse.of(activityPage, pagingSetup.pageSizeMessage());

    return pagingResponse;
  }

  @Override
  public SliceResponse getAllActivitiesSlice(PagingRequest request) {
    PagingSetup pagingSetup = buildPagingSetup(request);

    Slice<LearningActivity> allByVisibility = activityRepository
        .findAllByVisibility(pagingSetup.visibility(),
            pagingSetup.pageable());
    Slice<ActivityResponse> activitySlice = allByVisibility
        .map(ActivityResponse::from);
    boolean hasNext = activitySlice.hasNext();

    return SliceResponse.of(activitySlice, pagingSetup.pageSizeMessage(), hasNext);
  }

  @Override
  public List<ActivityResponse> getSearchActivities(SearchRequest request) {
    Visibility visibility = visibilityPublicValidator(request.visibility());

    if(request.category() != null) {
     return from(activityRepository.findByCategoryAndVisibility(
         request.category(),
         visibility
     ));
    }
    if (request.keyword() != null && !request.keyword().isBlank()) {
      return from(activityRepository.findByTitleContainingIgnoreCaseAndVisibility(
          request.keyword(),visibility
      ));
    }
    if(request.minMinutes() != null) {
      return from(activityRepository.findByMinutesGreaterThanEqualAndVisibility(
          request.minMinutes(),
          visibility
      ));
    }

    return from(activityRepository.findByVisibility(visibility));
  }

  @Override
  @Transactional
  public ActivityResponse addTags(Long activityId, AddTagRequest request) {

    LearningActivity activity = activityRepository.findById(activityId)
        .orElseThrow(()
            -> new ActivityNotFoundException(activityId));
    request.tags().forEach(activity::addTag);

    LearningActivity saved = activityRepository.save(activity);

    return ActivityResponse.from(saved);
  }

  @Override
  public boolean tagExistence(Long activityId, String tag) {
    LearningActivity activity = activityRepository.findById(activityId).
        orElseThrow(() -> new ActivityNotFoundException(activityId));

    boolean hasTag = activity.hasTag(tag);

    return hasTag;
  }

  @Override
  @Transactional
  public void deleteTag(Long activityId, String tag) {

    LearningActivity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new ActivityNotFoundException(activityId));

     activity.removeTag(tag);

  }


  //page, visibility, sort 검증 헬퍼 메서드
  private PagingSetup buildPagingSetup(PagingRequest request) {
    int requestedSize = request.requestedSize() == null ? 5 : request.requestedSize();

    PageSizeResult pageSizeResult = PageSizeValidator.resolvePageSize(requestedSize);

    String sortRequest = request.sort();
    int page = request.page() == null ? 0 : request.page();
    Visibility visibility = visibilityPublicValidator(request.visibility());

    if( page < 0 ) {
      throw new PageValidateFailureException(page);
    }

    String sortField = switch (sortRequest == null ? "id" : sortRequest) {
      case "minutes" -> "minutes";
      case "title" -> "title";
      case "id" -> "id";
      default -> throw new PagingRequestException(sortRequest);
    };

    Sort.Direction direction = request.direction() == null
        ? Sort.Direction.DESC
        : request.direction();

    Sort sort = Sort.by(direction, sortField);
    Pageable pageable = PageRequest.of(page, pageSizeResult.size(), sort);

   return PagingSetup.of(pageable, visibility,pageSizeResult.message() );
  }

  // List<LearningActivity>를 List<ActivityResponse>로 변환하는 헬퍼 메서드
  private  List<ActivityResponse> from(List<LearningActivity> activity) {
    List<ActivityResponse> activityResponses = activity.stream()
        .map(ActivityResponse::from)
        .toList();

    return activityResponses;
  }

//  //visibility 정규화 헬퍼
//  private Visibility visibilityVaildator(Visibility visibility) {
//    if(visibility == null) {
//      visibility = Visibility.PUBLIC;
//    } else if( visibility == Visibility.PRIVATE ) {
//      throw new VisibilityNotPublicException(visibility);
//    }
//    return visibility;
//  }

}
