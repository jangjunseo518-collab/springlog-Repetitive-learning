package com.springlog.repetitivelearning.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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

  private final AuthenticationEventListener listener = new AuthenticationEventListener();

  @Test
  void 인증_성공_이벤트를_받으면_사용자_이메일이_로그에_남는다(CapturedOutput output) {
    Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
        "test@test.com", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

    listener.onSuccess(new AuthenticationSuccessEvent(authentication));

    assertThat(output).contains("인증 성공").contains("test@test.com");
  }

  @Test
  void 인증_실패_이벤트를_받으면_시도한_이메일이_로그에_남고_비밀번호는_남지_않는다(CapturedOutput output) {
    Authentication authentication =
        UsernamePasswordAuthenticationToken.unauthenticated("test@test.com", "wrongPassword1");

    listener.onFailure(new AuthenticationFailureBadCredentialsEvent(
        authentication, new BadCredentialsException("Bad credentials")));

    assertThat(output).contains("인증 실패").contains("test@test.com");
    assertThat(output).doesNotContain("wrongPassword1");
  }
}
