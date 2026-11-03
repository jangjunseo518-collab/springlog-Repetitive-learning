package com.springlog.repetitivelearning.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(OutputCaptureExtension.class)
public class AuthenticationProcessTest {

  private static final String URL = "/api/v1/auth/whoami";
  private static final String NICKNAME = "tester";
  private static final String EMAIL = "auth-test@test.com";
  private static final String PASSWORD = "password123";

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
   void setUp() {
    userRepository.deleteAll();
    String encode = passwordEncoder.encode(PASSWORD);
    userRepository.save(new User(NICKNAME, EMAIL, encode));
  }
  
  @Test
  @DisplayName("인증 통과시 200과 사용자 정보 반환")
  void 올바른_이메일과_비밀번호면_200과_사용자_정보를_돌려준다() throws Exception {

    mockMvc.perform(get(URL)
        .with(httpBasic(EMAIL, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(EMAIL));
  }

  @Test
  @DisplayName("인증 시 틀린 비밀번호면 401을 반환")
  void 비밀번호가_틀리면_401을_반환한다() throws Exception {

    mockMvc.perform(get(URL)
            .with(httpBasic(EMAIL, "incorrectPassword")))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("인증 시 없는 이메일이면 401을 반환")
  void 이메일이_없으면_401을_반환한다() throws Exception {

    mockMvc.perform(get(URL)
            .with(httpBasic("nonExistent@Email.com", PASSWORD)))
        .andExpect(status().isUnauthorized());
  }
  
  @Test
  @DisplayName("틀린 비밀번호, 없는 이메일로 인증시 각가 응답이 동일하다.")
  void 비밀번호_틀림과_없는_이메일의_응답이_완전히_같다() throws Exception {
    // given
    MvcResult incorrectPassword = mockMvc.perform(get(URL)
            .with(httpBasic(EMAIL, "incorrectPassword")))
        .andReturn();
    MvcResult nonExistentEmail = mockMvc.perform(get(URL)
            .with(httpBasic("nonExistent@Email.com", PASSWORD)))
        .andReturn();

    MockHttpServletResponse passwordResponse = incorrectPassword.getResponse();
    MockHttpServletResponse emailResponse = nonExistentEmail.getResponse();

    // when & then
    assertThat(passwordResponse.getStatus()).isEqualTo(emailResponse.getStatus());
    assertThat(passwordResponse.getHeader("WWW-Authenticate"))
        .isEqualTo(emailResponse.getHeader("WWW-Authenticate"));
    assertThat(passwordResponse.getContentAsString())
        .isEqualTo(emailResponse.getContentAsString());
  }

  @Test
  void 인증_실패_로그의_이유도_두_경우가_같다(CapturedOutput output) throws Exception {
    mockMvc.perform(get(URL)
        .with(httpBasic(EMAIL, "wrongPassword1")));
    mockMvc.perform(get(URL)
        .with(httpBasic("nobody@test.com", PASSWORD)));

    assertThat(output).contains(
        "시도한 이메일: " + EMAIL + ", 이유: Bad credentials");
    assertThat(output).contains(
        "시도한 이메일: nobody@test.com, 이유: Bad credentials");
    assertThat(output).doesNotContain("wrongPassword1");
  }

}
