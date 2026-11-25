package com.springlog.repetitivelearning.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ActivityController.class)
@Import(SecurityConfig.class)
class ActivityAccessControlTest {

  @Autowired
  MockMvc mockMvc;
  @MockitoBean
  ActivityService activityService;
  @MockitoBean
  ActivityDashboard dashboard;

  @Test
  void 비로그인으로_수정_API를_호출하면_401과_AU001을_돌려준다() throws Exception {
    mockMvc.perform(patch("/api/activities/1/public"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value("AU001"));
  }

  @Test
  void 비로그인으로_조회_API를_호출하면_통과한다() throws Exception {
    mockMvc.perform(get("/api/activities/1"))
        .andExpect(status().isOk());
  }

  @Test
  void 비로그인으로_첨부_다운로드를_호출하면_401이다() throws Exception {
    mockMvc.perform(get("/api/activities/1/attachment/download"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void 비로그인_상태로_첨부파일_조회는_보안을_통과한다() throws Exception {
    when(activityService.findAttachmentUrl(
        1L, LoginUser.ANONYMOUS))
        .thenReturn(Optional.of("/api/files/sample.png"));

    mockMvc.perform(get("/api/activities/1/attachment"))
        //비로그인시 조회 테스트이기 때문에 요청에 User 정보를 포함하지 않는다.
        .andExpect(status().isFound())// 302
        .andExpect(header().string("Location", "/api/files/sample.png"));
  }

  @Test
  void 로그인으로_첨부파일_다운로드는_보안을_통과한다() throws Exception {

    User user = new User("테스터", "test@test.com", "password123!");
    ReflectionTestUtils.setField(user, "id", 1L);

    when(activityService.findDownloadUrl(
        eq(1L), any(LoginUser.class)))
        .thenReturn(Optional.of("/api/files/download/sample.png"));

    mockMvc.perform(get("/api/activities/1/attachment/download")
        .with(user(new CustomUserDetails(user))))
                               // 로그인시 요청에 대한 검증이기 때문에 요청에 User정보를 포함한다.
        .andExpect(status().isFound())
        .andExpect(header().string("Location", "/api/files/download/sample.png"));
  }

  @Test
  void 로그인하면_수정_API가_보안을_통과한다() throws Exception {
    User user = new User("테스터", "tester@email.com", "password123");
    ReflectionTestUtils.setField(user, "id", 1L);
    CustomUserDetails principal = new CustomUserDetails(user);

    mockMvc.perform(patch("/api/activities/1/public").with(user(principal)))
        .andExpect(status().isOk());
  }
}
