package com.springlog.repetitivelearning.service;

import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public interface ActivityDashboard {

 List<ActivityResponse> findByTag(String tag, Visibility visibility);

 Map<ActivityCategory, List<ActivityResponse>> groupByCategory(Visibility visibility);

 Map<ActivityCategory, Long> countByCategory(Visibility visibility);
 Set<String> getSortByTags(Visibility visibility);

}