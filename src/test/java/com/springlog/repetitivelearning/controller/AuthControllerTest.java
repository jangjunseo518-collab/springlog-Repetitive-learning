package com.springlog.repetitivelearning.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.springlog.repetitivelearning.config.SecurityConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  void 로그인하지_않고_whoami를_호출하면_401이다() throws Exception {
    mockMvc.perform(get("/api/v1/auth/whoami"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "test@test.com", roles = "USER")
  void 로그인한_사용자가_whoami를_호출하면_이메일과_권한을_반환한다() throws Exception {
    mockMvc.perform(get("/api/v1/auth/whoami"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("test@test.com"))
        .andExpect(jsonPath("$.authorities[0]").value("ROLE_USER"));
  }
}
