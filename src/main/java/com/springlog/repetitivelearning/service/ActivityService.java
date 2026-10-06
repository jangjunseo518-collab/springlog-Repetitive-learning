package com.springlog.repetitivelearning.service;

import com.springlog.repetitivelearning.dto.request.AddTagsRequest;
import com.springlog.repetitivelearning.dto.request.ChangeTitleRequest;
import com.springlog.repetitivelearning.dto.request.CreateActivityRequest;
import com.springlog.repetitivelearning.dto.request.IncreaseMinutesRequest;
import com.springlog.repetitivelearning.dto.request.PagingRequest;
import com.springlog.repetitivelearning.dto.request.SearchRequest;
import com.springlog.repetitivelearning.dto.response.ActivityResponse;
import com.springlog.repetitivelearning.dto.response.PageResponse;
import com.springlog.repetitivelearning.dto.response.SliceResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface ActivityService {

  ActivityResponse createActivity(CreateActivityRequest request, MultipartFile file);

  ActivityResponse getActivity(Long activityId);

  List<ActivityResponse> getActivitiesByOwnerId(Long ownerId);

  ActivityResponse addTags(Long activityId , AddTagsRequest request);
  boolean tagExistence(Long activityId, String tag);
  void deleteTag(Long activityId, String tag);

  PageResponse getAllPublicActivitiesByPage(PagingRequest request);
  SliceResponse getAllPublicActivitiesBySlice(PagingRequest response);

  List<ActivityResponse> getAllPublicActivities(SearchRequest searchRequest);

  ActivityResponse changeTitle(Long activityId, ChangeTitleRequest request);
  ActivityResponse increaseMinutes(Long activityId, IncreaseMinutesRequest request);
  ActivityResponse changeToPublic(Long activityId);
  ActivityResponse changeToPrivate(Long activityId);
  void deleteActivity(Long activityId);

  Optional<String> findAttachmentUrl(Long activityId);
  Optional<String> findDownloadUrl(Long activityId);


}
