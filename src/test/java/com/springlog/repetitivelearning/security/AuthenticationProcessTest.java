package com.springlog.repetitivelearning.security;

import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
@ActiveProfiles("test")
public class AuthenticationProcessTest {

  private static final String URL = "/api/v1/auth/whoami";
  private static final String nickName = "tester";
  private static final String email = "test@test.com";
  private static final String password = "password123";

  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
    userRepository.save(new User(
        nickName,
        email,
        passwordEncoder.encode(password)));
  }
  
  @Test
  @DisplayName("인증 성공시 200과 사용자 정보 응답")
  void 올바른_이메일과_비밀번호면_200과_사용자_정보를_돌려준다() throws Exception {
    mockMvc.perform(get(URL).with(httpBasic(email, password)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(email));
  }

  @Test
  @DisplayName("비밀번호가 틀리면 401응답")
  void 비밀번호가_틀리면_401로_응답한다() throws Exception {
    mockMvc.perform(get(URL)
            .with(httpBasic(email, "incorrectPassword")))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("없는 이메일이면 401응답")
  void 없는_이메일이면_401로_응답한다() throws Exception {
    mockMvc.perform(get(URL)
            .with(httpBasic("incorrect@email.com", password)))
        .andExpect(status().isUnauthorized());
  }
  
  @Test
  @DisplayName("유효하지 않은 비밀번호/이메일은 둘다 응답이 동일하다")
  void 비밀번호_틀림과_없는_이메일의_응답이_완전히_같다() throws Exception {

    MvcResult incorrectPassword = mockMvc.perform(get(URL)
            .with(httpBasic(email, "incorrectPassword")))
        .andReturn();
    MvcResult incorrectEmail = mockMvc.perform(get(URL)
            .with(httpBasic("incorrect@email.com", password)))
        .andReturn();

    MockHttpServletResponse a = incorrectEmail.getResponse();
    MockHttpServletResponse b = incorrectPassword.getResponse();

    assertThat(a.getStatus()).isEqualTo(b.getStatus());
    assertThat(a.getHeader("WWW-Authenticate"))
        .isEqualTo(b.getHeader("WWW-Authenticate"));
    assertThat(a.getContentAsString())
        .isEqualTo(b.getContentAsString());
  }

  @Test
  void 인증_실패_로그의_이유도_두_경우가_같다(CapturedOutput output) throws Exception {
    mockMvc.perform(get(URL).with(httpBasic(email, "wrongPassword1")));
    mockMvc.perform(get(URL).with(httpBasic("nobody@test.com", password)));

    assertThat(output).contains(
        "입력 이메일: " + email + ", 실패 사유: Bad credentials");
    assertThat(output).contains(
        "입력 이메일: nobody@test.com, 실패 사유: Bad credentials");
    assertThat(output).doesNotContain("wrongPassword1");
  }

}
