package com.springlog.repetitivelearning.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.springlog.repetitivelearning.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("로그인 없이 whoami호출시 401반환")
  void 로그인_없이_whoami호출시에_401_반환()throws Exception {
    // when & then
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
