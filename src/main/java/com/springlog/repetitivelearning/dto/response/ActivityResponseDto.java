package com.springlog.repetitivelearning.dto.response;

import com.springlog.repetitivelearning.domain.LearningActivity;
import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import java.util.Set;

public record ActivityResponseDto(
    long id,
    String title,
    int minutes,
    Visibility visibility,
    ActivityCategory activityCategory,
    Set<String> tags,
    String instructorName,
    Integer completionRate,
    String bookTitle,
    Long ownerId,
    String ownerNickName
) {
   public static ActivityResponseDto from(LearningActivity activity) {
     User owner = activity.getOwner();
     return new ActivityResponseDto(
         activity.getId(),
         activity.getTitle(),
         activity.getMinutes(),
         activity.getVisibility(),
         activity.getCategory(),
         activity.getTags(),
         activity.getInstructorName(),
         activity.getCompletionRate(),
         activity.getBookTitle(),
         owner.getId(),
         owner.getNickname()
     );

   }

  }


