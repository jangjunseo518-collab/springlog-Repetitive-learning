package com.springlog.repetitivelearning.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "activities")
public class LearningActivity extends BasicEntity {

  // 공통 필드
  @Column(nullable = false)
  private String title;
  @Column(nullable = false)
  private int minutes;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Visibility visibility;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ActivityCategory category;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "activity_tags", joinColumns = @JoinColumn(name = "activity_id"))
  @Column(name = "tag")
  private Set<String> tags = new HashSet<>();

  // 카테고리 별 속성
  @Column(length = 20)
  private String instructorName;
  private Integer completionRate;
  @Column(length = 20)
  private String bookTitle;

  public LearningActivity(String title, int minutes, Visibility visibility,
      ActivityCategory category, String instructorName, Integer completionRate, String bookTitle) {
    validateTitle(title);
    validateMinutes(minutes);
    this.title = title.trim();
    this.minutes = minutes;
    this.visibility = visibility;
    this.category = category;
    this.instructorName = instructorNameNormalization(category, instructorName);
    this.completionRate = completionRateNormalization(completionRate);
    this.bookTitle = bookTitleNormalization(category, bookTitle);
  }

// ========== 제목 & 학습 시간 ==========

  // 제목 유효성 검증
  private static void validateTitle(String title) {
    if (title == null || title.isBlank()) {
      throw new IllegalArgumentException("제목은 비워둘 수 없습니다.");
    }
  }
  // 학습 시간 유효성 검증
  private static void validateMinutes(int minutes) {
    if (minutes < 1) {
      throw new IllegalArgumentException("학습 시간은 1분 이상이여야 합니다.");
    }
  }

  //제목 변경
  public void changeTitle(String title) {
    validateTitle(title);
    this.title = title.trim();
  }
  // 학습 시간 변경
  public void changeMinutes(int minutes) {
    validateMinutes(minutes);
    this.minutes = minutes;
  }

  //공개 여부 변경
  public void changeToPublic() {
    this.visibility = Visibility.PUBLIC;
  }
  public void changeToPrivate() {
    this.visibility = Visibility.PRIVATE;
  }

  // ========== 태그 ==========

  //태그 저장
  public void addTag(String tag) {
    if(tags.size() >= 10){
      throw new IllegalArgumentException("태그는 10개까지 추가할 수 있습니다.");
    }
    String standardizedTag = tag.trim().toLowerCase();

    if(standardizedTag.length() > 20) {
      throw new IllegalArgumentException("태그의 길이는 20글자 까지 입력이 가능합니다.");
    }

    if(standardizedTag.matches("^[a-zA-Z가-힣0-9@#-]+$")){
      throw new IllegalArgumentException("태그는 한글,영문,숫자,#,@,-만 작성 가능합니다.");
    }

    this.tags.add(standardizedTag);
  }

  // 특정 태그 존재 확인
  public boolean hasTag(String tag) {
    if(tag == null || tag.isBlank()) {
      return false;
    }
    return tags.contains(tag.trim().toLowerCase());
  }

  // 태그 제거
  public void removeTag(String tag) {
    if(tag == null || tag.isBlank()) {
      return;
    }
    tags.remove(tag.trim().toLowerCase());
  }

  //읽기 전용 태그 목록 조회
  public Set<String> getTags() {
    return Collections.unmodifiableSet(this.tags);
  }


  // 카테고리 별 정규화
  private static String instructorNameNormalization(ActivityCategory category, String instructorName) {
    if(category == ActivityCategory.LECTURE && (instructorName == null || instructorName.isBlank())) {
      return "강사 미정";
    }
    return instructorName;
  }

  private static Integer completionRateNormalization(Integer completionRate) {
    if(completionRate == null) {
      return null; // 강의나 독서 카테고리라면 빈 값이 들어갈지도? 일단 보류.
    }
    if(completionRate < 0) {
      return 0;
    }
    if(completionRate > 100) {
      return 100;
    }
    return completionRate;
  }

  private static String bookTitleNormalization(ActivityCategory category ,String bookTitle) {
    if(category == ActivityCategory.READING && (bookTitle == null || bookTitle.isBlank())) {
      return "책 미정";
    }
    return bookTitle;
  }

}
