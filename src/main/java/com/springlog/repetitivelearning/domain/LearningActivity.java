package com.springlog.repetitivelearning.domain;

import com.springlog.repetitivelearning.domain.enums.ActivityCategory;
import com.springlog.repetitivelearning.domain.enums.Visibility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "activitys")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LearningActivity extends BasicEntity{

  //========== 공통 필드
  @Column(nullable = false, length = 20)
  private String title;
  @Column(nullable = false)
  private int minutes;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Visibility visibility;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ActivityCategory category;

  //========== 카테고리 속성
  @Column(length = 20)
  private String instructorName;
  private Integer completionRate;
  @Column(length = 20)
  private String bookTitle;

  public LearningActivity(String title, int minutes, Visibility visibility,
      ActivityCategory category,
      String instructorName, Integer completionRate, String bookTitle) {
    validateTitle(title);
    validateMinutes(minutes);
    this.title = title.trim();
    this.minutes = minutes;
    this.visibility = visibility;
    this.category = category;
    this.instructorName = instructorNameNormalization(category, instructorName);
    this.completionRate = completionRateNormalization(category, completionRate);
    this.bookTitle = bookTitleNormalization(category, bookTitle);
  }

  // title과 minutes 값 유효성 검증
  private static void validateTitle(String title) {
    if (title == null || title.isBlank()) {
      throw new IllegalArgumentException("제목은 비워둘 수 없습니다");
    }
  }
  //minutes 값 유효성 검증
  private static void validateMinutes(int minutes) {
    if (minutes < 1) {
      throw new IllegalArgumentException("학습 시간은 1분 이상이여야 합니다.");
    }
  }

  //========== 학습시간 증가
  public void increaseMinutes(int minutes) {
    validateMinutes(minutes);
    this.minutes += minutes;
  }
  //========== 제목 변경
  public void changeTitle(String title) {
    validateTitle(title);
    this.title = title.trim();
  }
  //========== 공개 상태 변경
  public void changeToPublic() {
    this.visibility = Visibility.PUBLIC;
  }
  public void changeToPrivate() {
    this.visibility = Visibility.PRIVATE;
  }

  //========== 카테고리 정규화
  //강사 이름 정규화
  private static String instructorNameNormalization(ActivityCategory category, String instructorName) {
    if(category == ActivityCategory.LECTURE && (instructorName == null || instructorName.isBlank())) {
      return "강사 미정";
    }
    return instructorName;
  }
  //완료율 정규화
  private static Integer completionRateNormalization(ActivityCategory category, Integer completionRate) {
    if(category != ActivityCategory.PRACTICE){
      return completionRate;
    }
    if(completionRate == null){
      return 0;
    }
    if(completionRate < 0) {
      return 0;
    }else if(completionRate > 100) {
      return 100;
    }
    return completionRate;
  }
  //책 제목 정규화
  private static String bookTitleNormalization(ActivityCategory category, String bookTitle) {
    if(category == ActivityCategory.READING && (bookTitle == null || bookTitle.isBlank())) {
      return "책 미정";
    }
    return bookTitle;
  }



}
