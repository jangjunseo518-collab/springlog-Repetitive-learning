package com.springlog.repetitivelearning.servic.impl;

import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.dto.request.SignUpRequest;
import com.springlog.repetitivelearning.dto.response.UserResponse;
import com.springlog.repetitivelearning.exception.BusinessException;
import com.springlog.repetitivelearning.exception.ErrorCode;
import com.springlog.repetitivelearning.repository.UserRepository;
import com.springlog.repetitivelearning.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

  @Mock
  UserRepository userRepository;

  @Mock
  PasswordEncoder passwordEncoder;

  @InjectMocks
  UserServiceImpl userService;

  private final SignUpRequest request =
      new SignUpRequest("test@test.com", "password123", "tester");

  @Test
  @DisplayName("회원가입하면 비밀번호는 해시되어 저장되며 SignUpRequest의 값이 정상적으로 사용된다")
  void 회원가입하면_비밀번호는_해시되어_저장되며_SignUpRequest의_값이_정상적으로_사용된다() {
    // given
    given(userRepository.existsByEmail("test@test.com")).willReturn(false);
    given(passwordEncoder.encode("password123"))
                                                                .willReturn("hashedPassword");
    given(userRepository.save(any(User.class)))
                                 .willAnswer(inv -> inv.getArgument(0));

    // when
    UserResponse response = userService.register(request);

    // then
    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(captor.capture());
    User saved = captor.getValue();

    //DB 저장 검증
    assertThat(saved.getPassword()).isEqualTo("hashedPassword");   // 해시가 저장됨
    assertThat(saved.getPassword()).isNotEqualTo("password123");   // 평문이 아님
    assertThat(saved.getEmail()).isEqualTo("test@test.com");
    assertThat(saved.getNickName()).isEqualTo("tester");
    //응답으로 반환되는 값 검증
    assertThat(response.email()).isEqualTo("test@test.com");
    assertThat(response.nickName()).isEqualTo("tester");
  }

  @Test
  @DisplayName("이메일 중복시 예외를 던지고 User를 저장하지 않는다")
  void 이메일이_이미_있으면_예외가_발생하고_저장하지_않는다() {
    // given
    given(userRepository.existsByEmail("test@test.com")).willReturn(true);

    // when & then
    assertThatThrownBy(() -> userService.register(request))
        .isInstanceOfSatisfying(BusinessException.class,
            e -> assertThat(e.getErrorCode())
                .isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS));

    verify(userRepository, never()).save(any());
  }

}
