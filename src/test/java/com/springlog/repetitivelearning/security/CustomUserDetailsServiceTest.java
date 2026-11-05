package com.springlog.repetitivelearning.security;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {


  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CustomUserDetailsService detailsService;

  @Test
  @DisplayName("이메일로 사용자를 찾으면 UserDetails를 반환한다")
  void 이메일로_사용자를_찾으면_UserDetails를_반환한다() {
    // given
    User user = new User("tester", "test@test.com", "hashedPassword");
    given(userRepository.findByEmail(user.getEmail()))
        .willReturn(Optional.of(user));

    // when
    UserDetails result = detailsService.loadUserByUsername("test@test.com");

    // then
    assertThat(result.getUsername()).isEqualTo("test@test.com");
    assertThat(result.getPassword()).isEqualTo("hashedPassword");

    assertThat(result.getAuthorities())
        .extracting(GrantedAuthority::getAuthority)
        .containsExactly("ROLE_USER");
  }

  @Test
  @DisplayName("없는 이메일로 사용자를 찾으면 예외를 던진다")
  void 없는_이메일로_사용자를_찾으면_예외를_던지다() {
    // given
    given(userRepository.findByEmail("nonExistent@Email.com"))
        .willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> detailsService.loadUserByUsername("nonExistent@Email.com"))
        .isInstanceOf(UsernameNotFoundException.class);
  }

}