package com.springlog.repetitivelearning.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.springlog.repetitivelearning.config.SecurityConfig;
import com.springlog.repetitivelearning.domain.type.Role;
import com.springlog.repetitivelearning.dto.request.SignUpRequest;
import com.springlog.repetitivelearning.dto.response.UserResponse;
import com.springlog.repetitivelearning.exception.BusinessException;
import com.springlog.repetitivelearning.exception.ErrorCode;
import com.springlog.repetitivelearning.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
// @WebMvcTest import는 아래 설명 참고

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  UserService userService;

  @Test
  void 회원가입에_성공하면_201과_Location_헤더와_응답을_돌려준다() throws Exception {
    given(userService.register(any(SignUpRequest.class)))
        .willReturn(new UserResponse(1L, "test@test.com", "tester", Role.USER));

    mockMvc.perform(post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"email":"test@test.com","password":"password123","nickName":"tester"}
                """))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/api/v1/users/1"))
        .andExpect(jsonPath("$.email").value("test@test.com"))
        .andExpect(jsonPath("$.nickName").value("tester"))
        .andExpect(jsonPath("$.role").value("USER"))
        .andExpect(jsonPath("$.password").doesNotExist());
  }

  @Test
  void 비밀번호가_7자이면_400을_돌려주고_서비스는_호출하지_않는다() throws Exception {
    mockMvc.perform(post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"email":"test@test.com","password":"1234567","nickName":"tester"}
                """))
        .andExpect(status().isBadRequest());

    verify(userService, never()).register(any());
  }

  @Test
  void 이메일이_중복이면_409를_돌려준다() throws Exception {
    given(userService.register(any(SignUpRequest.class)))
        .willThrow(new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS));

    mockMvc.perform(post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"email":"test@test.com","password":"password123","nickName":"tester"}
                """))
        .andExpect(status().isConflict());
  }
}
