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
import java.util.Locale;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.Collection;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "activities")
public class LearningActivity extends BasicEntity {

  // 공통 필드
  @Column(nullable = false)
  private String title;
  @Column(nullable = false)
  private int minutes;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "activity_tags", joinColumns = @JoinColumn(name = "activity_id"))
  @Column(name = "tag")
  private Set<String> tags = new HashSet<>();

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Visibility visibility;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ActivityCategory category;

  // 카테고리 전용 속성
  private String instructorName;
  private Integer completionRate;
  private String bookTitle;

  public LearningActivity(Visibility visibility, ActivityCategory category, String instructorName,
      Integer completionRate, String bookTitle, String title, int minutes) {
    validateTitle(title);
    validateMinutes(minutes);
    this.title = title.trim();
    this.bookTitle = bookTitleNormalization(category, bookTitle);
    this.visibility = visibility;
    this.category = category;
    this.instructorName = instructorNameNormalization(category, instructorName);
    this.completionRate = completionRateNormalization(category, completionRate);
    this.minutes = minutes;
  }

  // 제목, 학습시간 유효성 검증
  private static void validateTitle(String title) {
    if (title == null || title.isBlank()) {
      throw new IllegalArgumentException("제목을 입력해 주세요.");
    }
  }
  private static void validateMinutes(int minutes) {
    if (minutes < 1) {
      throw new IllegalArgumentException("학습시간은 1분 이상이여야 합니다.");
    }

  }

  //필수 필드 공통
  //제목 변경
  public void changeTitle(String title) {
    validateTitle(title);
    this.title = title.trim();
  }
  public void increaseMinutes(int minutes) {
    validateMinutes(minutes);
    this.minutes += minutes;
  }

  //공개 여부 변경
  public void changeToPublic() {
    this.visibility = Visibility.PUBLIC;
  }
  public void changeToPrivate() {
    this.visibility = Visibility.PRIVATE;
  }

  // ========= 태그

  public void addTag(String tag) {
    if(tags.size() >= 10) {
      throw new IllegalArgumentException("태그는 최대 10개까지 추가 가능합니다.");
    }

    String normalizationTag = tag.trim().toLowerCase();

    if(normalizationTag.length() > 20) {
      throw new IllegalArgumentException("태그는 20자까지 입력 가능합니다.");
    }

    if(!normalizationTag.matches("^[a-zA-Z가-힣0-9@#-]+$")) {
      throw new IllegalArgumentException("태그는 한글,영문,숫자,#,@,-만 입력 가능합니다.");
    }
    tags.add(normalizationTag);
  }

  public boolean removeTag(String tag) {
    if(tag == null || tag.isBlank()) {
      return false;
    }
   return tags.remove(tag.trim().toLowerCase());
  }

  public boolean hasTag(String tag) {
    if(tag == null || tag.isBlank()) {
      return false;
    }
    return tags.contains(tag.trim().toLowerCase());
  }

  public Set<String> getTags() {
    return Collections.unmodifiableSet(tags);
  }



  // ========== 카태고리 별 속성 정규화
  private static String instructorNameNormalization(ActivityCategory category, String instructorName) {
    if(category == ActivityCategory.LECTURE && (instructorName == null || instructorName.isBlank())) {
      return "강사미정";
    }
    return instructorName;
  }

  private static Integer completionRateNormalization(ActivityCategory category, Integer completionRate) {
    if(category != ActivityCategory.PRACTICE){
      return null; //일관성을 위해 캍고리 검증
    }
    if(completionRate == null) {
      return null;
    }
    if(completionRate < 0) {
      return 0;
    }
    if(completionRate > 100) {
      return 100;
    }
    return completionRate;
  }

  private static String bookTitleNormalization(ActivityCategory category, String bookTitle) {
    if(category == ActivityCategory.READING && (bookTitle == null || bookTitle.isBlank())) {
      return "책 미정";
    }
    return bookTitle;
  }
}
