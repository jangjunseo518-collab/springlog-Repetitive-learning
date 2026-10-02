package com.springlog.repetitivelearning.service;

import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.response.ActivityCountResponse;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ActivityDashboard {
  //테크로 조회
  List<ActivityResponse> findByTag(String tag, Visibility visibility);
  //카테고리 별 그룹화
  Map<ActivityCategory, List<ActivityResponse>> groupByCategory(Visibility visibility);
  //카테고리별 개수
  ActivityCountResponse countByCategory(Visibility visibility);
  //테그 오름차순 정렬
  Set<String> getSortByTags(Visibility visibility);
}
