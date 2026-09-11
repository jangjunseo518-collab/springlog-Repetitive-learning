package com.springlog.repetitivelearning;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.repository.ActivityRepository;
import com.springlog.repetitivelearning.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final ActivityRepository activityRepository;

  @Override
  public void run(String... args) {
    User owner = new User("철수", "email@email.com");
    userRepository.save(owner);

    LearningActivity activity = new LearningActivity(
        "스프링 공부", 60, Visibility.PUBLIC, ActivityCategory.LECTURE,
        "김강사", null,null);
    activity.assignOwner(owner);
    activityRepository.save(activity);

    System.out.println("초기 데이터 생성 완료, owner id = " + owner.getId());

  }
}
