package com.springlog.repetitivelearning.security;

import com.springlog.repetitivelearning.exception.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {

  private final HandlerExceptionResolver resolver;


  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) {

    resolver.resolveException(request, response, null,
        new ForbiddenException());

  }
}
