package com.springlog.repetitivelearning.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Role;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.request.ChangeTitleRequest;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.exception.BusinessException;
import com.springlog.repetitivelearning.exception.ErrorCode;
import com.springlog.repetitivelearning.exception.ForbiddenException;
import com.springlog.repetitivelearning.repository.ActivityRepository;
import com.springlog.repetitivelearning.repository.AuditLogRepository;
import com.springlog.repetitivelearning.repository.UserRepository;
import com.springlog.repetitivelearning.security.LoginUser;
import com.springlog.repetitivelearning.service.file.FileStorage;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ActivityServiceImplAccessControlTest {

  @Mock
  ActivityRepository activityRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  FileStorage fileStorage;
  @Mock
  AuditLogRepository auditLogRepository;

  @InjectMocks
  ActivityServiceImpl activityService;

  //활동 주인과, 비소유 사용자, 관리자를 미리 만들어 둠
  private LoginUser owner = new LoginUser(1L, Role.USER);
  private LoginUser other = new LoginUser(2L, Role.USER);
  private LoginUser admin = new LoginUser(3L, Role.ADMIN);

  //활동 객체를 만드는 헬퍼
  private LearningActivity activityOwnedBy(Long ownerId, Visibility visibility) {
    User activityOwner = new User("소유자", "owner@test.com", "hashPassword");
    ReflectionTestUtils.setField(activityOwner, "id", ownerId);

    LearningActivity activity = new LearningActivity("제목", 60, LocalDate.now(), visibility,
        ActivityCategory.PRACTICE, null, null, null);

    activity.assignOwner(activityOwner);

    return activity;
  }

  //위 헬퍼를 이용해서 활동객체를 만들고 첨부 파일을 추가하는 헬퍼
  private LearningActivity activityWithAttachment(Long ownerId, Visibility visibility) {
    LearningActivity activity = activityOwnedBy(ownerId, visibility);
    activity.attachmentFile("saved.png");

    return activity;
  }

  //소유권 검사 4개 테스트
  @Test
  void 소유자는_제목을_변경할_수_있다() {
    //given
    LearningActivity activity = activityOwnedBy(1L, Visibility.PUBLIC);
    when(activityRepository.findById(10L))
        .thenReturn(Optional.of(activity));
    when(activityRepository.save(any(LearningActivity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    //when
    activityService.changeTitle(
        10L, new ChangeTitleRequest("새 제목"), owner);

    //then
    assertThat(activity.getTitle()).isEqualTo("새 제목");
  }

  @Test
  void 관리자는_비소유한_활동_제목도_변경할_수_있다() {
    //given
    LearningActivity activity = activityOwnedBy(1L, Visibility.PUBLIC);
    when(activityRepository.findById(10L))
        .thenReturn(Optional.of(activity));
    when(activityRepository.save(any(LearningActivity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    //when
    activityService.changeTitle(
        10L, new ChangeTitleRequest("새 제목"), admin);

    //then
    assertThat(activity.getTitle()).isEqualTo("새 제목");
  }

  @Test
  void 소유자가_아닌_사용자의_제목_변경은_Forbidden이고_저장하지_않는다() {
    LearningActivity activity = activityOwnedBy(1L, Visibility.PUBLIC);
    when(activityRepository.findById(10L))
        .thenReturn(Optional.of(activity));

    //when & then
    assertThatThrownBy(
        () -> activityService.changeTitle(
            10L, new ChangeTitleRequest("새 제목"), other))
        .isInstanceOf(ForbiddenException.class);
    verify(activityRepository, never()).save(any());
  }

  @Test
  void 소유자가_아닌_사용자의_삭제는_Forbidden이고_파일과_활동을_지우지_않는다() {
    //given
    LearningActivity activity = activityOwnedBy(1L, Visibility.PUBLIC);
    when(activityRepository.findById(10L))
        .thenReturn(Optional.of(activity));

    //when & then
    assertThatThrownBy(
        () -> activityService.deleteActivity(10L, other))
        .isInstanceOf(ForbiddenException.class);
    verify(fileStorage, never()).deleteFile(any());
    verify(activityRepository, never()).delete(any());

  }

  //비공개 첨부 정책 5개 테스트

  @Test
  void 공개_활동의_첨부조회는_비로그인도_조회할_수_있다() {
    //given
    when(activityRepository.findById(10L))
        .thenReturn(Optional.of(
            activityWithAttachment(1L, Visibility.PUBLIC))
        );

    when(fileStorage.getFileUrl("saved.png"))
        .thenReturn("/api/files/saved.png");

    //when
    Optional<String> attachmentUrl =
        activityService.findAttachmentUrl(10L, LoginUser.ANONYMOUS);

    //then
    assertThat(attachmentUrl).contains("/api/files/saved.png");
  }

  @Test
  void 비공개_활동의_첨부조회는_비로그인이면_UNAUTHORIZED이다() {
    //given
    when(activityRepository.findById(10L))
        .thenReturn(Optional.of(
            activityWithAttachment(1L, Visibility.PRIVATE))
        );

    //when & then
    assertThatThrownBy(
        () ->
            activityService.findAttachmentUrl(10L, LoginUser.ANONYMOUS))
        .isInstanceOfSatisfying(BusinessException.class,
            e -> e.getErrorCode().equals(ErrorCode.UNAUTHORIZED)
        );
    verifyNoInteractions(fileStorage);
  }

  @Test
  void 비공개_활동의_첨부조회는_소유자가_아닌_사용자면_Forbidden이다() {
    //given
    when(activityRepository.findById(10L))
        .thenReturn(
            Optional.of(activityWithAttachment(1L, Visibility.PRIVATE))
        );

    //when & then

    assertThatThrownBy(
        () -> activityService.findAttachmentUrl(10L, other)
    ).isInstanceOf(ForbiddenException.class);
    verifyNoInteractions(fileStorage);
  }

  @Test
  void 비공개_활동의_첨부조회는_소유자가_조회할_수_있다() {
    //given
    when(activityRepository.findById(10L))
        .thenReturn(
            Optional.of(activityWithAttachment(1L, Visibility.PRIVATE))
        );
    when(fileStorage.getFileUrl("saved.png"))
        .thenReturn("/api/files/saved.png");

    //when
    Optional<String> attachmentUrl = activityService.findAttachmentUrl(10L, owner);

    //then
    assertThat(attachmentUrl).contains("/api/files/saved.png");
  }

  @Test
  void 비공개_활동의_첨부조회는_관리자가_조회할_수_있다() {
    //given
    when(activityRepository.findById(10L))
        .thenReturn(
            Optional.of(activityWithAttachment(1L, Visibility.PRIVATE))
        );
    when(fileStorage.getFileUrl("saved.png"))
        .thenReturn("/api/files/saved.png");

    //when
    Optional<String> attachmentUrl = activityService.findAttachmentUrl(10L, admin);

    //then
    assertThat(attachmentUrl).contains("/api/files/saved.png");
  }

  // 비공개 상세 조회 5개 테스트

  @Test
  void 공개_활동의_상세는_비로그인도_조회할_수_있다() {
    //given
    when(activityRepository.findById(10L))
        .thenReturn(Optional.of(activityOwnedBy(1L, Visibility.PUBLIC)));

    //when & then
    Optional<String> attachmentUrl =
        activityService.findAttachmentUrl(10L, LoginUser.ANONYMOUS);
    assertThat(attachmentUrl).isNotNull();
  }

  @Test
  void 비공개_활동의_상세는_비로그인이면_UNAUTHORIZED이다() {
    //given
    when(activityRepository.findById(10L))
        .thenReturn(Optional.of(activityOwnedBy(1L, Visibility.PRIVATE)));

    //when & then
    assertThatThrownBy(
        () -> activityService.getActivity(10L, LoginUser.ANONYMOUS)
    ).isInstanceOfSatisfying(BusinessException.class,
        e-> e.getErrorCode().equals(ErrorCode.UNAUTHORIZED));
  }

  @Test
  void 비공개_활동의_상세는_소유자가_아닌_사용자면_Forbidden이다() {
    //given
    when(activityRepository.findById(10L))
        .thenReturn(Optional.of(activityOwnedBy(1L, Visibility.PRIVATE)));

    //when & then
    assertThatThrownBy(
        () -> activityService.getActivity(10L, other))
        .isInstanceOf(ForbiddenException.class);
  }

  @Test
  void 소유자는_소유한_비공개_활동을_상세조회할_수_있다() {
    //given
    when(activityRepository.findById(10L))
        .thenReturn(Optional.of(activityOwnedBy(1L, Visibility.PRIVATE)));

    //when & then
    assertThat(activityService.getActivity(10L, owner))
        .isNotNull();
  }

  @Test
  void 비공개_활동의_상세는_관리자가_조회할_수_있다() {
    //given
    when(activityRepository.findById(10L))
        .thenReturn(Optional.of(activityOwnedBy(1L, Visibility.PRIVATE)));

    //when & then
    assertThat(activityService.getActivity(10L, admin))
        .isNotNull();
  }

  //태그 존재 확인 2개 테스트

  @Test
  void 공개_활동의_태그_존재는_비로그인도_확인할_수_있다() {
    //given
    LearningActivity activity = activityOwnedBy(1L, Visibility.PUBLIC);
    activity.addTag("java");
    when(activityRepository.findById(10L)).thenReturn(Optional.of(activity));

    //when & then
    assertThat(activityService.tagExistence(
        10L, "java", LoginUser.ANONYMOUS)).isTrue();
  }

  @Test
  void 비공개_활동의_태그_존재는_소유자가_아닌_사용자면_Forbidden이다(){
    //given
    LearningActivity activity = activityOwnedBy(1L, Visibility.PRIVATE);
    activity.addTag("java");
    when(activityRepository.findById(10L))
        .thenReturn(Optional.of(activity));

    //when & then
    assertThatThrownBy(
        () -> activityService.tagExistence(10L, "java", other)
    ).isInstanceOf(ForbiddenException.class);
  }

  //소유자별 목록 4개 테스트

  @Test
  void 본인의_목록은_비공개_활동까지_전부_돌려준다() {
    //given
    when(activityRepository.findByOwnerId(1L))
        .thenReturn(List.of(
            activityOwnedBy(1L, Visibility.PUBLIC),
            activityOwnedBy(1L, Visibility.PRIVATE)));

    //when & then
    List<ActivityResponse> activities =
        activityService.getActivitiesByOwnerId(1L, owner);
    assertThat(activities).hasSize(2);
  }

  @Test
  void 관리자는_남의_목록도_비공개_활동까지_전부_볼_수_있다() {
    //given
    when(activityRepository.findByOwnerId(1L))
        .thenReturn(List.of(
            activityOwnedBy(1L, Visibility.PUBLIC),
            activityOwnedBy(1L, Visibility.PRIVATE)));

    //when & then
    List<ActivityResponse> activities =
        activityService.getActivitiesByOwnerId(1L, admin);
    assertThat(activities).hasSize(2);
  }

  @Test
  void 다른_사용자는_남의_목록에서_공개_활동만_본다() {
    //given
    when(activityRepository.findByOwnerIdAndVisibility(1L, Visibility.PUBLIC))
        .thenReturn(List.of(activityOwnedBy(1L, Visibility.PUBLIC)));

    //when
    List<ActivityResponse> activities = activityService.getActivitiesByOwnerId(1L, other);

    // then
    assertThat(activities).hasSize(1);
    verify(activityRepository, never()).findByOwnerId(any());
  }

  @Test
  void 비로그인은_목록에서_공개_활동만_본다() {
    //given
    when(activityRepository.findByOwnerIdAndVisibility(1L, Visibility.PUBLIC))
        .thenReturn(List.of(activityOwnedBy(1L, Visibility.PUBLIC)));

    //when
    List<ActivityResponse> activities =
        activityService.getActivitiesByOwnerId(1L, LoginUser.ANONYMOUS);

    // then
    assertThat(activities).hasSize(1);
    verify(activityRepository, never()).findByOwnerId(any());
  }



}