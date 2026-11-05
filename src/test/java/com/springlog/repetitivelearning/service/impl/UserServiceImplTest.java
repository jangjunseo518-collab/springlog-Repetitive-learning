package com.springlog.repetitivelearning.service.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.dto.request.SignUpRequest;
import com.springlog.repetitivelearning.dto.response.UserResponse;
import com.springlog.repetitivelearning.exception.BusinessException;
import com.springlog.repetitivelearning.exception.ErrorCode;
import com.springlog.repetitivelearning.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserServiceImpl userService;

  private final SignUpRequest request = new SignUpRequest(
      "test@test.com",
      "password",
      "tester"
  );

  @Test
  @DisplayName("가입시 비밀번호 해시 저장 및 SignUpRequest값을 User생성과 응답 객체에 사용")
  void 가입시_비밀번호_해시_저장과_SignUpRequest의_값을_User생성과_응답에_사용한다() {
    // given
    given(userRepository.existsByEmail(request.email()))
        .willReturn(false);
    given(passwordEncoder.encode(request.password()))
        .willReturn("hashedPassword");
    given(userRepository.save(any(User.class)))
        .willAnswer(inv -> inv.getArgument(0));
  
    // when
    UserResponse response = userService.register(request);

    // then
    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(captor.capture());
    User user = captor.getValue();

    assertThat(user.getEmail()).isEqualTo(request.email());
    assertThat(user.getNickName()).isEqualTo(request.nickName());
    assertThat(user.getPassword()).isNotEqualTo(request.password());
    assertThat(user.getPassword()).isEqualTo("hashedPassword");

    //응답
    assertThat(response.email()).isEqualTo(request.email());
    assertThat(response.nickName()).isEqualTo(request.nickName());
  }

  @Test
  @DisplayName("이메일 중복시에는 save가 호출되지 않는다")
  void 이메일_중복_시에는_예외를_던지고_save가_호출되지_않는다() {
    // given
    given(userRepository.existsByEmail(request.email()))
        .willReturn(true);

    // when & then
    assertThatThrownBy(() -> userService.register(request))
        .isInstanceOfSatisfying(BusinessException.class,
            e -> assertThat(e.getErrorCode())
                .isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS));
    verify(userRepository, never()).save(any(User.class));
  }


}