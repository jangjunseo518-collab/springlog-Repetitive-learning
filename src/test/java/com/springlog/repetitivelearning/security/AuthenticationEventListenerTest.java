package com.springlog.repetitivelearning.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@ExtendWith(OutputCaptureExtension.class)
class AuthenticationEventListenerTest {
  
  private AuthenticationEventListener listener =  new AuthenticationEventListener();
  
  @Test
  @DisplayName("인증 성공시 성공 로그를 만든다")
  void 인증_성공_이벤트를_바다으면_사용자_이메일이_로그에_남는다(CapturedOutput output) {
    // given
    Authentication authenticated = UsernamePasswordAuthenticationToken.authenticated(
        "test@test.com",
        null,
        List.of(new SimpleGrantedAuthority("ROLE_USER"))
    );

    // when
    AuthenticationSuccessEvent successEvent = new AuthenticationSuccessEvent(
        authenticated);
    listener.onSuccess(successEvent);

    // then
    assertThat(output).contains("인증 성공").contains("test@test.com");
  }
  
  @Test
  @DisplayName("인증 실패시 실패 로그를 만든다")
  void 인증_실패_이벤트를_받으면_시도한_이메일이_로그에_남고_비밀번호는_남지_않는다(CapturedOutput output) {
    // given
    Authentication unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(
        "incorrect@Email.com", "password123!"
    );

    // when
    AuthenticationFailureBadCredentialsEvent credentialsEvent =
        new AuthenticationFailureBadCredentialsEvent(
            unauthenticated,
            new BadCredentialsException("인증에 실패했습니다.")
        );
    listener.onFailure(credentialsEvent);

    // then
    assertThat(output).contains("인증 실패").contains("incorrect@Email.com");
    assertThat(output).doesNotContain("password123!");

    
  }

}