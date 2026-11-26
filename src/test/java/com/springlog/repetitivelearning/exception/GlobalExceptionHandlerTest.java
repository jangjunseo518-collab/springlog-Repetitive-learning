package com.springlog.repetitivelearning.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  @DisplayName("ForbiddenException 에 응답은 403과 FORBIDDEN코드이다.")
  void ForbiddenException의_웅답은_403_FORBIDDEN코드이다() {
    // given
    ProblemDetail pd = handler.handleBusinessException(
        new ForbiddenException("권한이 없습니다."));

    // when & then
    assertThat(pd.getStatus()).isEqualTo(403);
    assertThat(pd.getProperties()).containsEntry("code","AU002");
    assertThat(pd.getDetail()).isEqualTo("권한이 없습니다.");
     
    
  }

}