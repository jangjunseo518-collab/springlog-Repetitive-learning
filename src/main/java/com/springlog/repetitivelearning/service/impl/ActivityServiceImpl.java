package com.springlog.repetitivelearning.service.impl;


import static com.springlog.repetitivelearning.service.helper.VisibilityValidator.visibilityPublicValidator;
import static com.springlog.repetitivelearning.service.helper.paging.PagingHelper.buildPagingSetup;

import com.springlog.repetitivelearning.domain.ActivityAuditLog;
import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.ActionCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.request.AddTagsRequest;
import com.springlog.repetitivelearning.dto.request.ChangeTitleRequest;
import com.springlog.repetitivelearning.dto.request.CreateActivityRequest;
import com.springlog.repetitivelearning.dto.request.IncreaseMinutesRequest;
import com.springlog.repetitivelearning.dto.request.PagingRequest;
import com.springlog.repetitivelearning.dto.request.SearchRequest;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.dto.response.PageResponse;
import com.springlog.repetitivelearning.dto.response.SliceResponse;
import com.springlog.repetitivelearning.exception.ActivityNotFoundException;
import com.springlog.repetitivelearning.exception.OwnerNotFoundException;
import com.springlog.repetitivelearning.repository.ActivityRepository;
import com.springlog.repetitivelearning.repository.AuditLogRepository;
import com.springlog.repetitivelearning.repository.UserRepository;
import com.springlog.repetitivelearning.service.ActivityService;
import com.springlog.repetitivelearning.service.file.FileStorage;
import com.springlog.repetitivelearning.service.helper.paging.PagingSetup;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.audit.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

  private final ActivityRepository activityRepository;
  private final UserRepository userRepository;
  private final FileStorage fileStorage;
  private final AuditLogRepository auditLogRepository;

  @Override
  @Transactional
  public ActivityResponse createActivity(CreateActivityRequest request,
      MultipartFile file) {
    Long ownerId = request.ownerId();
    User owner = userRepository.findById(ownerId).orElseThrow(
        () -> new OwnerNotFoundException(ownerId)
    );

    LearningActivity activity = new LearningActivity(request.title(), request.minutes(),
        request.studiedOn(), request.visibility(), request.category(),
        request.instructorName(), request.completionRate(), request.bookTitle());
    activity.assignOwner(owner);

    if(file != null &&  !file.isEmpty()) {
      String savedFile = fileStorage.saveFile(file);
      activity.attachmentFile(savedFile);
    }

    LearningActivity saved = activityRepository.save(activity);

    recordAuditLog(ActionCategory.CREATE,
        "활동 생성", saved);

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

    Set<String> beforeAddTags = new HashSet<>(activity.getTags());

    request.tags().forEach(activity::addTag);

    Set<String> addedTags = new HashSet<>(activity.getTags().stream()
        .filter(t -> !beforeAddTags.contains(t))
        .collect(Collectors.toSet()));

    LearningActivity saved = activityRepository.save(activity);

    recordAuditLog(ActionCategory.UPDATE,
        "태그 추가"
            +"\n기존 태그: " + beforeAddTags
            +"\n추가 태그: " + addedTags, saved);

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

    Set<String> beforeDeleteTags = new HashSet<>(activity.getTags());

    boolean removeTag = activity.removeTag(tag);

    Set<String> deleteTags = new HashSet<>(activity.getTags());


    List<String> removedTags = beforeDeleteTags.stream()
        .filter(t -> !deleteTags.contains(t))
        .toList();


    if(removeTag) {
      recordAuditLog(ActionCategory.UPDATE,
          "태그 삭제"
              + "\n기존 태그: " + beforeDeleteTags
              + "\n삭제 태그: " + removedTags, activity);
    }

  }

  @Override
  public PageResponse getAllPublicActivitiesByPage(PagingRequest request) {
    PagingSetup pagingSetup = buildPagingSetup(request);

    Page<LearningActivity> activities = activityRepository.findByVisibility(
        pagingSetup.visibility(), pagingSetup.pageable());

    Page<ActivityResponse> activitiesPage = activities.map(ActivityResponse::from);

    return PageResponse.of(activitiesPage, pagingSetup.adjustedMessage());

  }

  @Override
  public SliceResponse getAllPublicActivitiesBySlice(PagingRequest response) {

    PagingSetup pagingSetup = buildPagingSetup(response);

    Slice<LearningActivity> activities = activityRepository.findAllByVisibility(
        pagingSetup.visibility(), pagingSetup.pageable());

    Slice<ActivityResponse> activitiesSlice = activities.map(ActivityResponse::from);
    boolean hasNext = activitiesSlice.hasNext();

    return SliceResponse.of(activitiesSlice, pagingSetup.adjustedMessage(), hasNext);

  }

  @Override
  public List<ActivityResponse> getAllPublicActivities(SearchRequest searchRequest) {

    Visibility visibility = visibilityPublicValidator(searchRequest.visibility());

    if(searchRequest.category()!= null){
      List<LearningActivity> activities = activityRepository.findByCategoryAndVisibility(
          searchRequest.category(), visibility);
      return activities.stream().map(ActivityResponse::from).toList();
    }
    if(searchRequest.titleKeyword()!= null && !searchRequest.titleKeyword().isBlank()){
      List<LearningActivity> activities = activityRepository.findByTitleContainingIgnoreCaseAndVisibility(
          searchRequest.titleKeyword(), visibility);
      return activities.stream().map(ActivityResponse::from).toList();
    }
    if(searchRequest.minMinutes() != null){
      List<LearningActivity> activities = activityRepository.findByMinutesGreaterThanEqualAndVisibility(
          searchRequest.minMinutes(), visibility);
      return activities.stream().map(ActivityResponse::from).toList();
    }

    return activityRepository.findByVisibility(visibility).stream()
        .map(ActivityResponse::from).toList();
  }

  @Override
  @Transactional
  public ActivityResponse changeTitle(Long activityId, ChangeTitleRequest request) {
    LearningActivity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new ActivityNotFoundException(activityId));

    String beforeChangeTitle = activity.getTitle();

    activity.changeTitle(request.title());
    LearningActivity saved = activityRepository.save(activity);

    recordAuditLog(ActionCategory.UPDATE,
        "제목 변경"
                  +"\n변경 전 제목: " + beforeChangeTitle
                  +"\n변경 후 제목: " + saved.getTitle(), saved);


    return ActivityResponse.from(saved);
  }

  @Override
  @Transactional
  public ActivityResponse increaseMinutes(Long activityId, IncreaseMinutesRequest request) {
    LearningActivity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new ActivityNotFoundException(activityId));

    int beforeIncreaseMinutes = activity.getMinutes();

    activity.increaseMinutes(request.minutes());
    LearningActivity saved = activityRepository.save(activity);

    recordAuditLog(ActionCategory.UPDATE,
                   "학습 시간 증가"
                            +"\n기존 학습시간: " + beforeIncreaseMinutes
                            +"\n증가 학습시간: " + request.minutes(),saved);

    return ActivityResponse.from(saved);
  }

  @Override
  @Transactional
  public ActivityResponse changeToPublic(Long activityId) {
    LearningActivity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new ActivityNotFoundException(activityId));

   Visibility beforeChangeToPublic = activity.getVisibility();

    activity.changeToPublic();
    LearningActivity saved = activityRepository.save(activity);

    recordAuditLog(ActionCategory.UPDATE,
        "공개 활동으로 변경"
                 +"\n기존 공개여부: " + beforeChangeToPublic
                 +"\n변경 후 공개여부: " +  saved.getVisibility(), saved);

    return ActivityResponse.from(saved);
  }

  @Override
  @Transactional
  public ActivityResponse changeToPrivate(Long activityId) {
    LearningActivity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new ActivityNotFoundException(activityId));

    Visibility beforeChangeToPrivate = activity.getVisibility();

    activity.changeToPrivate();
    LearningActivity saved = activityRepository.save(activity);

    recordAuditLog(ActionCategory.UPDATE,
        "비공개 활동으로 변경"
            +"\n기존 공개여부: " + beforeChangeToPrivate
            +"\n변경 후 공개여부: " +  saved.getVisibility(), saved);


    return ActivityResponse.from(saved);
  }

  @Override
  @Transactional
  public void deleteActivity(Long activityId) {
    LearningActivity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new ActivityNotFoundException(activityId));

    String attachmentFile = activity.getAttachmentFile();
    fileStorage.deleteFile(attachmentFile);

    recordAuditLog(ActionCategory.DELETE,
        "활동 삭제", activity);

    activityRepository.delete(activity);
    log.info("활동 삭제 완료 id: {} ", activityId);
  }

  @Override
  public Optional<String> findAttachmentUrl(Long activityId) {
    return getStoredFileName(activityId).map(fileStorage::getFileUrl);
  }

  @Override
  public Optional<String> findDownloadUrl(Long activityId) {
    return getStoredFileName(activityId).map(fileStorage::getDownloadUrl);
  }


  //파일명 반환하는 헬퍼
  private Optional<String> getStoredFileName(Long activityId) {
    LearningActivity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new ActivityNotFoundException(activityId));

    String attachmentFile = activity.getAttachmentFile();

    if(attachmentFile == null ||  attachmentFile.isBlank()){
      return Optional.empty();
    }
    return Optional.of(attachmentFile);

  }

  private void recordAuditLog(ActionCategory actionCategory,
                            String description, LearningActivity activity) {
    String detail = description
        +"\n title: " + activity.getTitle()
        +"\n id: " + activity.getId()
        +"\n category: " + activity.getCategory()
        +"\n visibility: " + activity.getVisibility();
    String ownerId = "ownerId: " + activity.getOwner().getId();
    auditLogRepository.save(new ActivityAuditLog(actionCategory, detail, ownerId ));
  }

}
