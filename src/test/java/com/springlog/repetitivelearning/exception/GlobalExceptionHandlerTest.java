package com.springlog.repetitivelearning.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  @DisplayName("ForbiddenException의 응답 검증")
  void ForbiddenException은_403과_ErrorCode의_FORBIDDEN_코드로_응답한다() {
    // given
    ProblemDetail pd = handler.handleBusinessException(
        new ForbiddenException("수정 권한이 없습니다.")
    );

    // when & then
    assertThat(pd.getStatus()).isEqualTo(403);
    assertThat(pd.getProperties()).containsEntry("code", "AU002");
    assertThat(pd.getDetail()).isEqualTo("수정 권한이 없습니다.");

  }

}