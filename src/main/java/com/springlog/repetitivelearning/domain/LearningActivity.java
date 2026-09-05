package com.springlog.repetitivelearning.domain;

import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "activities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LearningActivity extends BasicEntity {

  // 공통 필드
  @Column(nullable = false)
  private String title;
  @Column(nullable = false)
  private int minutes;

  @Column(nullable = false)
  private Visibility visibility;
  @Column(nullable = false)
  private ActivityCategory category;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "activity_tags", joinColumns = @JoinColumn(name = "activity_id"))
  @Column(name = "tag")
  private Set<String> tags = new HashSet<>();

  // 카테고리 속성
  private String instructorName;
  private Integer completionRate;
  private String bookTitle;

  public LearningActivity(String title, int minutes, Visibility visibility,
      ActivityCategory category,
      String instructorName, Integer completionRate, String bookTitle) {
    validationTitle(title);
    validationMinutes(minutes);
    this.title = title;
    this.minutes = minutes;
    this.visibility = visibility;
    this.category = category;
    this.instructorName = instructorNameNormalization(category, instructorName);
    this.completionRate = completionRateNormalization(category, completionRate);
    this.bookTitle = bookTitleNormalization(category, bookTitle);
  }

  //========== 제목, 학습 시간 유효성 검증
  private static void validationTitle(String title) {
    if (title == null || title.isBlank()) {
      throw new IllegalArgumentException("제목은 비워둘 수 없습니다.");
    }
  }

  private static void validationMinutes(int minutes) {
    if (minutes < 1) {
      throw new IllegalArgumentException("학습 시간은 1분 이상이여야 합니다.");
    }
  }

  //========== 재목 변경
  public void changeTitle(String newTitle) {
    validationTitle(newTitle);
    this.title = newTitle.trim();
  }
  //========== 학습 시간 증가
  public void increaseMinutes(int minutes) {
    validationMinutes(minutes);
    this.minutes += minutes;
  }

  //========== 공개 여부 변경
  public void changeToPublic() {
    this.visibility = Visibility.PUBLIC;
  }
  public void changeToPrivate() {
    this.visibility = Visibility.PRIVATE;
  }



  //========== tag 추가
  public void addTag(String tag) {
    if(tag == null || tag.isBlank()) {
      throw new IllegalArgumentException("태그는 비워둘 수 없습니다.");
    }
    String normalized = tag.trim().toLowerCase();

    if(normalized.length() > 20) {
      throw new IllegalArgumentException("태그의 길이는 20자 이하만 가능합니다.");
    }

    if(tags.size() >= 10){
      throw new IllegalArgumentException("태그는 10개까지만 추가할 수 있습니다.");
    }

    if(!normalized.matches("^[a-zA-Z가-힣0-9@#-]+$")) {
      throw new IllegalArgumentException("한글,영문,숫자,@,#,-만 입력 가능합니다.");
    }

    this.tags.add(normalized);
  }

  //========= tag 제거
  public boolean removeTag(String tag) {
    if(tag == null || tag.isBlank()) {
      return false;
    }
    return this.tags.remove(tag.trim().toLowerCase());
  }

  //========== tags 목록을 읽기 전용으로 변한
  public Set<String> getTags() {
    return Collections.unmodifiableSet(this.tags);
  }

  //========== tag 등록되어있는지 확인
  public boolean hasTag(String tag) {
    if(tag == null)return false;
    return this.tags.contains(tag.trim().toLowerCase());
  }


  //========== 카태고리별 전용 속성 정규화

  //강사이름 정규화
  private static String instructorNameNormalization(ActivityCategory category, String instructorName) {
    if(category == ActivityCategory.LECTURE && (instructorName == null || instructorName.isBlank())) {
    return "강사 미정";
    }
    return instructorName;
  }

  //완료율 정규화
  private static Integer completionRateNormalization(ActivityCategory category, Integer completionRate) {
    if(category != ActivityCategory.PRACTICE) {
      return completionRate;
    }
    if(completionRate == null) {
      return 0;// 어차피 카테고리가 실습이면 완료율이 빈거보단 0이 안전할 듯
    }
    if(completionRate < 0) {
      return 0;
    }
    if(completionRate > 100) {
      return 100;
    }
    return completionRate;
  }

  //책 제녹 정규화
  private static String bookTitleNormalization(ActivityCategory category, String bookTitle) {
    if(category == ActivityCategory.READING && (bookTitle == null || bookTitle.isBlank())) {
      return "책 미정";
    }
    return bookTitle;
  }
}
