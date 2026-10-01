package com.springlog.repetitivelearning;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.repository.ActivityRepository;
import com.springlog.repetitivelearning.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private static final String[] INSTRUCTORS = {"김강사", "이강사", "박강사"};
  private static final String[] TITLES = {"스프링", "JPA ", "Java "};
  private static final String[] BOOK_TITLES = {"웹개발", "백엔드", "C+", "C++", "리액트"};
  private static final String[] TAGS = {"데브옵", "알고리", "데이터", "프론트"};

  private final ActivityRepository activityRepository;
  private final UserRepository userRepository;

  @Override
  public void run(String... args) {
    // 1. 유저 3명 생성 및 저장
    List<User> users = List.of(
        new User("개발자A", "userA@email.com"),
        new User("개발자B", "userB@email.com"),
        new User("개발자C", "userC@email.com")
    );
    userRepository.saveAll(users);

    User[] userArray = users.toArray(new User[0]);

    // 출력을 위한 공개/비공개 분류 리스트
    List<LearningActivity> publicActivities = new ArrayList<>();
    List<LearningActivity> privateActivities = new ArrayList<>();

    // 2. 더미 활동 생성 및 저장
    for (int i = 1; i <= 10; i++) {
      User randomOwner = getRandomElement(userArray);
      String title = getRandomElement(TITLES);
      String instructor = getRandomElement(INSTRUCTORS);
      String bookTitle = getRandomElement(BOOK_TITLES);
      int durationMinutes = ThreadLocalRandom.current().nextInt(1, 1441);
      int completionRate = ThreadLocalRandom.current().nextInt(0, 101); // 0 ~ 100%

      Visibility visibility = getRandomEnum(Visibility.class);
      ActivityCategory category = getRandomEnum(ActivityCategory.class);

      // 엔티티 생성자 타입에 완벽하게 맞춘 생성
      LearningActivity activity = new LearningActivity(
          title,
          durationMinutes,
          LocalDate.now(),
          visibility,
          category,
          instructor,     // LECTURE 카테고리용
          completionRate, // PRACTICE 카테고리용 (Integer)
          bookTitle       // READING 카테고리용
      );

      activity.assignOwner(randomOwner);

      // 태그는 별도 addTag 메서드로 1~2개 임의 추가
      activity.addTag(getRandomElement(TAGS));
      if (ThreadLocalRandom.current().nextBoolean()) {
        activity.addTag(getRandomElement(TAGS));
      }

      activityRepository.save(activity);

      if (visibility == Visibility.PUBLIC) {
        publicActivities.add(activity);
      } else {
        privateActivities.add(activity);
      }
    }

    // 3. 지정된 순서로 출력 (활동ID, 제목, 강사, 시간, 카테고리, 공개여부, 오너ID, 태그)
    System.out.println("==========[공개 활동 목록 (PUBLIC)]==========");
    for (LearningActivity act : publicActivities) {
      System.out.printf("활동ID: %d | 제목: %s | 강사: %s | 시간: %d분 | 카테고리: %s | 공개여부: %s | 오너ID: %d | 태그: %s%n",
          act.getId(),
          act.getTitle(),
          act.getInstructorName(),
          act.getMinutes(),
          act.getCategory(),
          act.getVisibility(),
          act.getOwner().getId(),
          act.getTags());
    }

    System.out.println("\n==========[비공개 활동 목록 (PRIVATE)]==========");
    for (LearningActivity act : privateActivities) {
      System.out.printf("활동ID: %d | 제목: %s | 강사: %s | 시간: %d분 | 카테고리: %s | 공개여부: %s | 오너ID: %d | 태그: %s%n",
          act.getId(),
          act.getTitle(),
          act.getInstructorName(),
          act.getMinutes(),
          act.getCategory(),
          act.getVisibility(),
          act.getOwner().getId(),
          act.getTags());
    }
  }

  private <T> T getRandomElement(T[] array) {
    int index = ThreadLocalRandom.current().nextInt(array.length);
    return array[index];
  }

  private <T extends Enum<T>> T getRandomEnum(Class<T> enumClass) {
    T[] enumValues = enumClass.getEnumConstants();
    int index = ThreadLocalRandom.current().nextInt(enumValues.length);
    return enumValues[index];
  }
}