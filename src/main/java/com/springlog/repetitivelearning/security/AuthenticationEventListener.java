package com.springlog.repetitivelearning.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEventListener {

  @EventListener
  public void onSuccess(AuthenticationSuccessEvent event) {
    log.info("[AUTH] 인증 성공 - 사용자: {}", event.getAuthentication().getName());
  }

  @EventListener
  public void onFailure(AbstractAuthenticationFailureEvent event) {
    log.warn("[AUTH] 인증 실패 - 시도한 이메일: {}, 이유: {}",
        event.getAuthentication().getName(),
        event.getException().getMessage());
  }
}
