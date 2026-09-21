package com.springlog.repetitivelearning.exception;

public class ActivityNotFoundException extends RuntimeException {

  public ActivityNotFoundException(Long activityId) {
    super("활동을 찾지 못 했습니다. activityId: " + activityId);
  }
}
