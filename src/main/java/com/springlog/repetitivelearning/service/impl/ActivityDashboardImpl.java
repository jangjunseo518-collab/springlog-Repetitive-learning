package com.springlog.repetitivelearning.service.impl;

import static com.springlog.repetitivelearning.service.helper.VisibilityValidator.visibilityPublicValidator;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.response.ActivityCountResponse;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.repository.ActivityRepository;
import com.springlog.repetitivelearning.service.ActivityDashboard;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ActivityDashboardImpl implements ActivityDashboard {

  private final ActivityRepository activityRepository;

  @Override
  public List<ActivityResponse> findByTag(String tag, Visibility visibility) {
    String trimTag = tag.trim();

    List<LearningActivity> activities = activityRepository.findByTagsContainingAndVisibility(
        trimTag, visibilityPublicValidator(visibility));
    List<ActivityResponse> activityResponses = activities.stream()
        .map(ActivityResponse::from).toList();

    return activityResponses;
  }

  @Override
  public Map<ActivityCategory, List<ActivityResponse>> groupByCategory(Visibility visibility) {

    Map<ActivityCategory, List<ActivityResponse>> groupCategory = new HashMap<>();

    List<LearningActivity> activities = activityRepository
        .findByVisibility(visibilityPublicValidator(visibility));

    for (LearningActivity activity : activities) {
      groupCategory.computeIfAbsent(activity.getCategory(),
          k -> new ArrayList<>())
          .add(ActivityResponse.from(activity));
    }
    return groupCategory;
  }

  @Override
  public ActivityCountResponse countByCategory(Visibility visibility) {

    Map<ActivityCategory, Long> countCategory = new EnumMap<>(ActivityCategory.class);

    List<LearningActivity> activities = activityRepository.findByVisibility(
        visibilityPublicValidator(visibility));

    for (ActivityCategory category : ActivityCategory.values()) {
      countCategory.put(category, 0L);
    }

    for (LearningActivity activity : activities) {
      countCategory.merge(activity.getCategory(), 1L, Long::sum);
    }

    long count = activities.size();

    return ActivityCountResponse.of(count, countCategory);
  }

  @Override
  public Set<String> getSortByTags(Visibility visibility) {

    Set<String> tags = new TreeSet<>();

    List<LearningActivity> activities = activityRepository.findByVisibility(
        visibilityPublicValidator(visibility));

    for (LearningActivity activity : activities) {
      tags.addAll(activity.getTags());
    }

    return tags;
  }
}
