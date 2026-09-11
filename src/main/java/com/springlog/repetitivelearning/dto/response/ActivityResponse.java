package com.springlog.repetitivelearning.dto.response;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import java.time.LocalDateTime;
import java.util.Set;

public record ActivityResponse(
    Long id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String title,
    int minutes,
    Set<String> tags,
    Visibility visibility,
    ActivityCategory category,
    String instructorName,
    Integer completionRate,
    String bookTitle,
    Long ownerId,
    String ownerNickname
) {

  public static ActivityResponse from(LearningActivity activity) {
    User owner = activity.getOwner();
    Long ownerId = (owner != null) ? owner.getId() : null;
    String ownerNickname = (owner != null) ? owner.getNickName() : null;

    return new ActivityResponse(
        activity.getId(),
        activity.getCreatedAt(),
        activity.getUpdatedAt(),
        activity.getTitle(),
        activity.getMinutes(),
        activity.getTags(),
        activity.getVisibility(),
        activity.getCategory(),
        activity.getInstructorName(),
        activity.getCompletionRate(),
        activity.getBookTitle(),
        ownerId,
        ownerNickname
    );
  }

}
