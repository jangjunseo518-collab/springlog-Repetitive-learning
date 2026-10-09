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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    if (file != null && !file.isEmpty()) {
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

    Set<String> beforeAddTag = new HashSet<>(activity.getTags());

    request.tags().forEach(activity::addTag);

    Set<String> afterAddTag = new HashSet<>(activity.getTags());
    afterAddTag.removeAll(beforeAddTag);

    LearningActivity saved = activityRepository.save(activity);

    recordAuditLog(ActionCategory.UPDATE,
        "태그 추가"
            + "\n 이전 태그: " + beforeAddTag
            + "\n 추가한 태그: " + afterAddTag, saved);

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

    Set<String> beforeDeleteTagTag = new HashSet<>(activity.getTags());

    boolean removeTag = activity.removeTag(tag);

    Set<String> afterTags = new HashSet<>(activity.getTags());

    List<String> deletedTags = beforeDeleteTagTag.stream()
        .filter(t -> !afterTags.contains(t))
        .toList();

    if (removeTag) {
      recordAuditLog(ActionCategory.UPDATE,
          "태그 삭제"
              + "\n 이전 태그: " + beforeDeleteTagTag
              + "\n 삭제한 태그: " + deletedTags, activity);
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

    if (searchRequest.category() != null) {
      List<LearningActivity> activities = activityRepository.findByCategoryAndVisibility(
          searchRequest.category(), visibility);
      return activities.stream().map(ActivityResponse::from).toList();
    }
    if (searchRequest.titleKeyword() != null && !searchRequest.titleKeyword().isBlank()) {
      List<LearningActivity> activities = activityRepository.findByTitleContainingIgnoreCaseAndVisibility(
          searchRequest.titleKeyword(), visibility);
      return activities.stream().map(ActivityResponse::from).toList();
    }
    if (searchRequest.minMinutes() != null) {
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

    String titleBeforeChange = activity.getTitle();

    activity.changeTitle(request.title());
    LearningActivity saved = activityRepository.save(activity);

    recordAuditLog(ActionCategory.UPDATE,
        "활동 제목 변경"
                +"\n변경 전 제목: " + titleBeforeChange
                +"\n변경 후 제목: " +  saved.getTitle(), saved);

    return ActivityResponse.from(saved);
  }

  @Override
  @Transactional
  public ActivityResponse increaseMinutes(Long activityId, IncreaseMinutesRequest request) {
    LearningActivity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new ActivityNotFoundException(activityId));

    int minutesBeforeChange = activity.getMinutes();

    activity.increaseMinutes(request.minutes());
    LearningActivity saved = activityRepository.save(activity);

    recordAuditLog(ActionCategory.UPDATE,
        "학습 시간 변경"
            +"\n변경 전 학습 시간: " + minutesBeforeChange
            +"\n변경 후 학습 시간: " +  saved.getMinutes(), saved);


    return ActivityResponse.from(saved);
  }

    @Override
    @Transactional
    public ActivityResponse changeToPublic(Long activityId) {
      LearningActivity activity = activityRepository.findById(activityId)
          .orElseThrow(() -> new ActivityNotFoundException(activityId));

      Visibility visibilityBeforeChange = activity.getVisibility();

      activity.changeToPublic();

      LearningActivity saved = activityRepository.save(activity);

     recordAuditLog(ActionCategory.UPDATE,
         "공개 활동으로 변경"
             + "\n변경 전 공개 여부: " + visibilityBeforeChange
             + "\n변경 후 공개 여부: " + saved.getVisibility(),  saved);
      return ActivityResponse.from(saved);
    }

    @Override
    @Transactional
    public ActivityResponse changeToPrivate(Long activityId) {
      LearningActivity activity = activityRepository.findById(activityId)
          .orElseThrow(() -> new ActivityNotFoundException(activityId));

      Visibility visibilityBeforeChange = activity.getVisibility();

      activity.changeToPrivate();

      LearningActivity saved = activityRepository.save(activity);

      recordAuditLog(ActionCategory.UPDATE,
          "비공개 활동으로 변경"
              + "\n변경 전 공개 여부: " + visibilityBeforeChange
              + "\n변경 후 공개 여부: " + saved.getVisibility(),  saved);


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

    if (attachmentFile == null || attachmentFile.isBlank()) {
      return Optional.empty();
    }
    return Optional.of(attachmentFile);

  }

  //auditLogBuilder
  private void recordAuditLog(ActionCategory action,
      String summary, LearningActivity activity) {

    String detail = summary
        + "\n활동 Title: " + activity.getTitle()
        + "\n활동 ID: " + activity.getId()
        + "\n활동 Category: " + activity.getCategory();
    String actor = "ownerId: " + activity.getOwner().getId();

    auditLogRepository.save(new ActivityAuditLog(action, detail, actor));


  }

}
