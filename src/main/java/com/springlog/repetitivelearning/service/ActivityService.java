package com.springlog.repetitivelearning.service;

import com.springlog.repetitivelearning.dto.request.AddTagRequest;
import com.springlog.repetitivelearning.dto.request.CreateActivityRequest;
import com.springlog.repetitivelearning.dto.request.PagingRequest;
import com.springlog.repetitivelearning.dto.request.SearchRequest;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.dto.response.PagingResponse;
import com.springlog.repetitivelearning.dto.response.SliceResponse;
import java.util.List;

public interface ActivityService {

  ActivityResponse createActivity(CreateActivityRequest request);

  ActivityResponse getActivity(Long activityId);

  List<ActivityResponse> getActivitiesByOwnerId(Long ownerId);

  PagingResponse getAllActivities(PagingRequest request);
  SliceResponse getAllActivitiesSlice(PagingRequest request);
  List<ActivityResponse> getSearchActivities(SearchRequest response);
  ActivityResponse addTags(Long activityId , AddTagRequest request);
  boolean tagExistence(Long activityId, String tag);
  void deleteTag(Long activityId, String tag);
}
