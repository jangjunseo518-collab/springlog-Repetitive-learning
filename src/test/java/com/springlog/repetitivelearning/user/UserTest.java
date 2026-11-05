package com.springlog.repetitivelearning.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.given;

import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.Role;
import com.springlog.repetitivelearning.exception.InvalidOwnerInputException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class UserTest {

  @Test
  @DisplayName("유저 생성시 Role 기본값은 USER이다")
  void 유저_생성시_ROLE이_USER이다() {
    // given
    User user = new User("tester", "test@test.com", "hashedPassword");


    // then
    assertThat(user.getNickName()).isEqualTo("tester");
    assertThat(user.getEmail()).isEqualTo("test@test.com");
    assertThat(user.getPassword()).isEqualTo("hashedPassword");
    assertThat(user.getRole()).isEqualTo(Role.USER);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", "     "})
  void 닉네임이_비어있으면_예외가_발생한다(String nickName) {
    
    assertThatThrownBy(() -> new User(
        nickName,
        "test@test.com",
        "hashedPassword"))
        .isInstanceOf(InvalidOwnerInputException.class);

  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", "     "})
  void 이메일이_비어있으면_예외가_발생한다(String email) {

    assertThatThrownBy(() -> new User(
        "tester",
        email,
        "hashedPassword"))
        .isInstanceOf(InvalidOwnerInputException.class);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", "     "})
  void 비밀번호가_비어있으면_예외가_발생한다(String password) {

    assertThatThrownBy(() -> new User(
        "tester",
        "test@test.com",
        password))
        .isInstanceOf(InvalidOwnerInputException.class);
  }



}
