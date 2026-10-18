package com.springlog.repetitivelearning.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  //Activity 예외코드
  ACTIVITY_NOT_FOUND("A001", HttpStatus.NOT_FOUND, "활동을 찾을 수 없습니다."),
  VISIBILITY_NOT_PUBLIC("A002", HttpStatus.BAD_REQUEST, "공개 활동 조회로는 공개활동만 조회가 가능합니다."),
  INVALID_ACTIVITY_INPUT("A003", HttpStatus.BAD_REQUEST, "활동 입력값이 올바르지 않습니다."),
  INVALID_TAG("A004", HttpStatus.BAD_REQUEST, "태그가 올바르지 않습니다."),


  //Owner 예외코드
  OWNER_NOT_FOUND("U001", HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
  INVALID_USER_INPUT("U002", HttpStatus.BAD_REQUEST, "사용자 입력값이 올바르지 않습니다."),


  // Paging 예외코드
  INVALID_SORT_FIELD("P001", HttpStatus.BAD_REQUEST, "정렬 기준이 올바르지 않습니다."),
  PAGE_VALIDATION_FAILURE("P002", HttpStatus.BAD_REQUEST, "페이지 요청값이 올바르지 않습니다."),

  // File 예외코드
  FILE_STORAGE_FAILED("F001", HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다."),
  INVALID_FILE("F002", HttpStatus.BAD_REQUEST, "파일이 올바르지 않습니다."),

  // Common 예외코드
  INVALID_INPUT("C001", HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
  INTERNAL_ERROR("C999", HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 문제가 발생했습니다.");


  private final String code;
  private final HttpStatus status;
  private final String defaultMessage;

}
