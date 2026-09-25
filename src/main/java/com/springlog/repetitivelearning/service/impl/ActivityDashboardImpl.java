package com.springlog.repetitivelearning.service.impl;

import static com.springlog.repetitivelearning.service.helper.HelperMethod.visibilityPublicValidator;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.repository.ActivityRepository;
import com.springlog.repetitivelearning.service.ActivityDashboard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityDashboardImpl implements ActivityDashboard {

  private final ActivityRepository activityRepository;

  @Override
  public List<ActivityResponse> findByTag(String tag, Visibility visibility) {

    List<LearningActivity> activitiesByTag =
        activityRepository.findByTagsContainingAndVisibility(
            tag, visibilityPublicValidator(visibility));

    List<ActivityResponse> activityResponse = activitiesByTag.stream()
        .map(ActivityResponse::from)
        .toList();

    return activityResponse;
  }

  @Override
  public Map<ActivityCategory, List<ActivityResponse>> groupByCategory(Visibility visibility) {

    List<LearningActivity> activities = activityRepository
        .findByVisibility(visibilityPublicValidator(visibility));

    Map<ActivityCategory, List<ActivityResponse>> groupCategory = new HashMap<>();

    for (LearningActivity activity : activities) {
      groupCategory.computeIfAbsent(activity.getCategory(),
          k -> new ArrayList<>())
          .add(ActivityResponse.from(activity));
    }


    return groupCategory;
  }

  @Override
  public Map<ActivityCategory, Long> countByCategory(Visibility visibility) {

    Map<ActivityCategory, Long> countByCategory = new HashMap<>();

    List<LearningActivity> activities = activityRepository.findByVisibility(
        visibilityPublicValidator(visibility));

    for (LearningActivity activity : activities) {
      countByCategory.merge(activity.getCategory(), 1L, Long::sum);
    }

    return countByCategory;
  }

  @Override
  public Set<String> getSortByTags(Visibility visibility) {

    Set<String> sortByTags = new TreeSet<>();

    List<LearningActivity> byVisibility = activityRepository.findByVisibility(
        visibilityPublicValidator(visibility));

    for (LearningActivity activity : byVisibility) {
      sortByTags.addAll(activity.getTags());
    }

    return sortByTags;
  }
}
