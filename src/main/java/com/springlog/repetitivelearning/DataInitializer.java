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

  private final ActivityRepository activityRepository;
  private final UserRepository userRepository;

  @Override
  public void run(String... args) {
    User owner = new User("닉네임", "email@email.com");
    userRepository.save(owner);

    LearningActivity activity = new LearningActivity("제목", 60, Visibility.PUBLIC,
        ActivityCategory.LECTURE,
        "이강사", null, null);
    activity.assignOwner(owner);
    activityRepository.save(activity);

    System.out.println("초기 데이터 저장 완료 ownerId:" + owner.getId());

  }
}
