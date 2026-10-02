package com.springlog.repetitivelearning.service.impl;

import static com.springlog.repetitivelearning.service.helper.VisibilityValidator.visibilityPublicValidator;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.repository.ActivityRepository;
import com.springlog.repetitivelearning.service.ActivityDashboard;
import java.util.ArrayList;
import java.util.EnumMap;
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
public class ActivityDashBoardImpl implements ActivityDashboard {

  private final ActivityRepository activityRepository;

  @Override
  public List<ActivityResponse> findByTag(String tag, Visibility visibility) {
    String trim = tag.trim();

    List<LearningActivity> activities = activityRepository.findByTagsContainingAndVisibility(
        trim, visibilityPublicValidator(visibility));

    List<ActivityResponse> activityResponses = activities.stream()
        .map(ActivityResponse::from).toList();

    return activityResponses;
  }

  @Override
  public Map<ActivityCategory, List<ActivityResponse>> groupByCategory(Visibility visibility) {

    Map<ActivityCategory, List<ActivityResponse>> groupCategory = new EnumMap<>(
                                                                ActivityCategory.class);
    List<LearningActivity> activities = activityRepository.findByVisibility(
        visibilityPublicValidator(visibility));

   for (LearningActivity activity : activities) {
     groupCategory.computeIfAbsent(activity.getCategory(),
         k -> new ArrayList<>())
         .add(ActivityResponse.from(activity));
   }

    return groupCategory;
  }

  @Override
  public Map<ActivityCategory, Long> countByCategroy(Visibility visibility) {

    Map<ActivityCategory, Long> countCategory = new EnumMap<>(ActivityCategory.class);

    List<LearningActivity> activities = activityRepository.findByVisibility(
        visibilityPublicValidator(visibility));

    for (ActivityCategory category : ActivityCategory.values()) {
      countCategory.put(category, 0L);
    }

    for (LearningActivity activity : activities) {
      countCategory.merge(activity.getCategory(), 1L, Long::sum);
    }



    return countCategory;
  }

  @Override
  public Set<String> sortByAllTags(Visibility visibility) {

    Set<String> sortTags = new TreeSet<>();

    List<LearningActivity> activities = activityRepository.findByVisibility(
        visibilityPublicValidator(visibility));

    for (LearningActivity activity : activities) {
      sortTags.addAll(activity.getTags());
    }

    return sortTags;
  }
}
