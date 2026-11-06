package com.springlog.repetitivelearning.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(OutputCaptureExtension.class)
class AuthenticationProcessTest {

  private static final String URL = "/api/v1/auth/whoami";
  private static final String EMAIL = "auth-test@test.com";
  private static final String PASSWORD = "password123";

  @Autowired MockMvc mockMvc;
  @Autowired UserRepository userRepository;
  @Autowired PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    userRepository.save(new User("tester", EMAIL, passwordEncoder.encode(PASSWORD)));
  }

  @Test
  void 올바른_이메일과_비밀번호면_200과_사용자_정보를_돌려준다() throws Exception {
    mockMvc.perform(get(URL)
            .with(httpBasic(EMAIL, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(EMAIL));
  }

  @Test
  void 비밀번호가_틀리면_401() throws Exception {
    mockMvc.perform(get(URL)
            .with(httpBasic(EMAIL, "wrongPassword1")))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void 없는_이메일이면_401() throws Exception {
    mockMvc.perform(get(URL)
            .with(httpBasic("nobody@test.com", PASSWORD)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void 비밀번호_틀림과_없는_이메일의_응답이_완전히_같다() throws Exception {
    MvcResult wrongPassword = mockMvc.perform(get(URL)
            .with(httpBasic(EMAIL, "wrongPassword1")))
        .andReturn();

    MvcResult unknownEmail = mockMvc.perform(get(URL)
            .with(httpBasic("nobody@test.com", PASSWORD)))
        .andReturn();

    MockHttpServletResponse a = wrongPassword.getResponse();
    MockHttpServletResponse b = unknownEmail.getResponse();

    assertThat(a.getStatus()).isEqualTo(b.getStatus());
    assertThat(a.getHeader("WWW-Authenticate")).isEqualTo(b.getHeader("WWW-Authenticate"));
    assertThat(a.getContentAsString()).isEqualTo(b.getContentAsString());
  }

  @Test
  void 인증_실패_로그의_이유도_두_경우가_같다(CapturedOutput output) throws Exception {
    mockMvc.perform(get(URL).with(httpBasic(EMAIL, "wrongPassword1")));
    mockMvc.perform(get(URL).with(httpBasic("nobody@test.com", PASSWORD)));

    assertThat(output).contains("시도한 이메일: " + EMAIL + ", 이유: Bad credentials");
    assertThat(output).contains("시도한 이메일: nobody@test.com, 이유: Bad credentials");
    assertThat(output).doesNotContain("wrongPassword1");
  }
}