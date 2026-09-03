package com.springlog.repetitivelearning.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LearningActivity extends BasicEntity {

  //=====공통 필드=====
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

  // 카테고리별 고유 속성 (필드)
  @Column(length = 30)
  private String instructorName;

  private Integer completionRate;

  @Column(length = 50)
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
    this.completionRate = completionRateNormalization(category, completionRate);
    this.bookTitle = bookTitleNormalization(category, bookTitle);
  }

  //================ 제목과 학습 시간의 유효성 검증
  private static void validateTitle(String title) {
    if(title == null || title.isBlank()) {
      throw new IllegalArgumentException("제목은 비우둘 수 없습니다.");
    }
  }
  private static void validateMinutes(int minutes) {
    if(minutes < 1) {
      throw new IllegalArgumentException("학습 시간은 1분 이상만 입력이 가능합니다.");
    }
  }

  //================ 공개여부 변경
  public void changeToPublic() {
    this.visibility = Visibility.PUBLIC;
  }
  public void changeToPrivate() {
    this.visibility = Visibility.PRIVATE;
  }

  //================ 제목 변경
  public void changeTitle(String newTitle) {
    if(newTitle == null || newTitle.isBlank()) {
      throw new IllegalArgumentException("제목은 비워둘 수 없습니다.");
    }
    this.title = newTitle;
  }

  //================ 학습 시간 증가
  public void increaseInStudyTime(int increaseMinutes) {
    if(increaseMinutes < 1) {
      throw new IllegalArgumentException("증가한 학습 시간은 1분 이상이여야 합니다");
    }
    this.minutes += increaseMinutes;
  }

  //================ 카테고리 별 전용 속성
  // 강사이름 정규화
  private static String instructorNameNormalization(ActivityCategory category , String instructorName) {
    if(category == ActivityCategory.LECTURE && (instructorName == null || instructorName.isBlank())) {
      return "강사 미정";
    }
    return instructorName;
  }

  // 학습 완료율 정규화
  private static Integer completionRateNormalization(ActivityCategory category , Integer completionRate) {
    if (category != ActivityCategory.PRACTICE){
      return completionRate;
    }
    if(completionRate == null) {
      return 0;
      // 완료율 미입력시 0으로 치환한다. -> null을 필드에 넣는 것보다 0으로 치환하는게 안전하다는 판단.
    }
    if (completionRate < 0) {
      return 0;
    } else if (completionRate > 100) {
      return 100;
    }

    return completionRate;
  }

  // 책 제목 정규화
  private static String bookTitleNormalization(ActivityCategory category, String bookTitle) {
    if(category == ActivityCategory.READING && (bookTitle == null || bookTitle.isBlank())) {
      return "책 미정";
    }
    return bookTitle;
  }



}
