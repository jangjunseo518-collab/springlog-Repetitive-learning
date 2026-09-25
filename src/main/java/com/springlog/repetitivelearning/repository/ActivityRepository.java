package com.springlog.repetitivelearning.repository;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<LearningActivity, Long> {

  List<LearningActivity> findByOwnerId(Long ownerId);

  Page<LearningActivity> findByVisibility(Visibility visibility, Pageable pageable);

  Slice<LearningActivity> findAllByVisibility(Visibility visibility, Pageable pageable);

  List<LearningActivity> findByTagsContainingAndVisibility(String tag, Visibility visibility);

  List<LearningActivity> findByCategoryAndVisibility(ActivityCategory category, Visibility visibility);
  List<LearningActivity> findByTitleContainingIgnoreCaseAndVisibility(String title, Visibility visibility);
  List<LearningActivity> findByMinutesGreaterThanEqualAndVisibility (int minutes, Visibility visibility);
  List<LearningActivity> findByVisibility(Visibility visibility);


}
