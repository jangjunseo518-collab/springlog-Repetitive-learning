package com.springlog.repetitivelearning.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.given;

import com.springlog.repetitivelearning.domain.type.Role;
import com.springlog.repetitivelearning.exception.InvalidOwnerInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class UserTest {

  @Test
  @DisplayName("User생성시 권한은 USER가 기본적용")
  void User생성시_기본권한_USER적용() {
    // given
    User user = new User(
        "tester", "test@test.com", "hashedPassword");

    // when & then
    assertThat(user.getNickName()).isEqualTo("tester");
    assertThat(user.getEmail()).isEqualTo("test@test.com");
    assertThat(user.getPassword()).isEqualTo("hashedPassword");
    assertThat(user.getRole()).isEqualTo(Role.USER);

  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void 유효하지_않은_닉네임은_예외가_발생한다(String nickName) {

    assertThatThrownBy(
        () -> new User(nickName,
            "test@test.com",
            "hashedPassword"))
        .isInstanceOf(InvalidOwnerInputException.class);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void 유효하지_않은_이메일은_예외가_발생한다(String email) {

    assertThatThrownBy(
        () -> new User("tester",
            email,
            "hashedPassword"))
        .isInstanceOf(InvalidOwnerInputException.class);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void 유효하지_않은_비밀반호는_예외가_발생한다(String password) {

    assertThatThrownBy(
        () -> new User("tester",
            "test@test.com",
            password))
        .isInstanceOf(InvalidOwnerInputException.class);
  }

}