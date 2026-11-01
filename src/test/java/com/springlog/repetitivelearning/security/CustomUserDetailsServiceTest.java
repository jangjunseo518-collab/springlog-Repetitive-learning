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
public class CustomUserDetailsServiceTest {
  
  @Mock
  private UserRepository userRepository;
  
  @InjectMocks
  private CustomUserDetailsService userDetailsService;

  @Test
  @DisplayName("이메일로 사용자를 조회하면 UserDetailes를 반환한다.")
  void 이메일로_사용자를_조회하면_이메일_비밀번호_권한이_담긴_UserDetails를_반환한다() {
    // given
    User user = new User("tester", "test@test.com", "hashedPassword");
    given(userRepository.findByEmail("test@test.com"))
        .willReturn(Optional.of(user));

    // when
    UserDetails result = userDetailsService.loadUserByUsername("test@test.com");


    // then
    assertThat(result.getUsername()).isEqualTo("test@test.com");
    assertThat(result.getPassword()).isEqualTo("hashedPassword");
    assertThat(result.getAuthorities())
        .extracting(GrantedAuthority::getAuthority)
        .containsExactly("ROLE_USER");
  }

  @Test
  @DisplayName("이메일이 앖으면 예외를 던진다.")
  void 이메일에_해당하는_사용자가_없으면_UsernameNotFoundException이_발생한다() {
    // given
    given(userRepository.findByEmail("none@test.com")).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> userDetailsService.loadUserByUsername("none@test.com"))
        .isInstanceOf(UsernameNotFoundException.class);
  }

}
