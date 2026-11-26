package com.springlog.repetitivelearning.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.springlog.repetitivelearning.config.SecurityConfig;
import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.security.CustomUserDetails;
import com.springlog.repetitivelearning.security.LoginUser;
import com.springlog.repetitivelearning.service.ActivityDashboard;
import com.springlog.repetitivelearning.service.ActivityService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ActivityController.class)
@Import(SecurityConfig.class)
public class ActivityAccessControlTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ActivityService service;
  @MockitoBean
  private ActivityDashboard dashboard;

  @Test
  void 비로그인으로_수정_API를_호출하면_401과_AU001을_돌려준다() throws Exception {
    mockMvc.perform(patch("/api/activities/1/public"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value("AU001"));
  }

  @Test
  void 비로그인으로_조회요청하면_통과한다() throws Exception {
    mockMvc.perform(get("/api/activities/1"))
        .andExpect(status().isOk());
  }

  @Test
  void 비로그인으로_첨부파일_다운_요청하면_401이다() throws Exception {
    mockMvc.perform(get("/api/activities//1/attachment/download"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void 비로그인으로_첨부파일_조회요청하면_통과한다() throws Exception {
    when(service.findAttachmentUrl(1L, LoginUser.ANONYMOUS))
        .thenReturn(Optional.of("/api/files/sample.png"));

    mockMvc.perform(get("/api/activities/1/attachment"))
        .andExpect(status().isFound())
        .andExpect(header().string("Location", "/api/files/sample.png"));
  }

  @Test
  void 로그인_후_첨부파일_다운요청은_보안을_통과한다() throws Exception {
    //given
    User user = new User("테스터", "tst@test.com", "hashPassword");
    ReflectionTestUtils.setField(user, "id", 1L);

    when(service.findDownloadUrl(eq(1L), any(LoginUser.class)))
        .thenReturn(Optional.of("/api/files/download/sample.png"));

    mockMvc.perform(get("/api/activities/1/attachment/download")
            .with(user(new CustomUserDetails(user))))
        .andExpect(status().isFound())
        .andExpect(header().string("Location", "/api/files/download/sample.png"));
  }

  @Test
  void 로그인하면_수정요청이_보안을_통과한다() throws Exception {
    //given
    User user = new User("테스터", "tst@test.com", "hashPassword");
    ReflectionTestUtils.setField(user, "id", 1L);

    CustomUserDetails principal = new CustomUserDetails(user);

    mockMvc.perform(patch("/api/activities/1/public")
            .with(user(principal)))
        .andExpect(status().isOk());
  }


}
