package com.springlog.repetitivelearning.exception;

public class ActivityNotFoundException extends BusinessException {

  public ActivityNotFoundException(Long activityId) {
    super(ErrorCode.ACTIVITY_NOT_FOUND, "활동을 찾지 못 했습니다. activityId: " + activityId);
  }
}
