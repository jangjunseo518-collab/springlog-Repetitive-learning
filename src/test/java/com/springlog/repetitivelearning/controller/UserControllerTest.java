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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private UserService userService;

  @Test
  @DisplayName("회원가입 성공시 Location 해더와 응답 반환")
  void 회원가입에_성공하면_201과_Location_헤더와_응답을_돌려준다() throws Exception {
    // given
    given(userService.register(any(SignUpRequest.class)))
        .willReturn(new UserResponse(
            1L, "test@test.com","tester", Role.USER));

    // when & then
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
  @DisplayName("요청 본문에 비밀번호가 7자면 서비스 호출하지 않는다")
  void 요청_본문에_비밀번호가_7자면_서비스를_호출하지_읺는다() throws Exception {

    mockMvc.perform(post("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
                {"email":"test@test.com","password":"1234567","nickName":"tester"}
                """))
        .andExpect(status().isBadRequest());

    verify(userService, never())
        .register(any());

  }

  @Test
  @DisplayName("이메일 중복시 409를 반환")
  void 이메일_중복시에는_409를_반환한다() throws Exception {
    // given
    given(userService.register(any(SignUpRequest.class)))
        .willThrow(new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS));

    // when  then
    mockMvc.perform(post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"email":"test@test.com","password":"password123!","nickName":"tester"}
                """))
        .andExpect(status().isConflict());


  }


}