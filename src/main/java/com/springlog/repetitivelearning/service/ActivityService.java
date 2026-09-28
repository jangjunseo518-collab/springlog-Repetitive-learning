package com.springlog.repetitivelearning.service;

import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.request.AddTagsRequest;
import com.springlog.repetitivelearning.dto.request.CreateActivityRequest;
import com.springlog.repetitivelearning.dto.request.PagingRequest;
import com.springlog.repetitivelearning.dto.request.SearchRequest;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.dto.response.PageResponse;
import com.springlog.repetitivelearning.dto.response.SliceResponse;
import java.util.List;
import org.springframework.data.domain.PageRequest;

public interface ActivityService {

  ActivityResponse createActivity(CreateActivityRequest request);

  ActivityResponse getActivity(Long activityId);

  List<ActivityResponse> getActivitiesByOwnerId(Long ownerId);

  ActivityResponse addTags(Long activityId , AddTagsRequest request);
  boolean tagExistence(Long activityId, String tag);
  void deleteTag(Long activityId, String tag);

  PageResponse getAllPublicActivitiesPage(PagingRequest pageRequest);
  SliceResponse getAllPublicActivitiesSlice(PagingRequest pageRequest);

  List<ActivityResponse> getSearchActivities(SearchRequest searchRequest);


}
