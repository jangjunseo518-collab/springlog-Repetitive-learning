package com.springlog.repetitivelearning.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.springlog.repetitivelearning.domain.type.Role;
import com.springlog.repetitivelearning.exception.InvalidOwnerInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class UserTest {

  @Test
  void 생성하면_기본_권한은_USER이고_닉네임과_이메일의_앞뒤_공백은_제거된다() {
    User user = new User("  tester  ", " test@test.com  ", "hashedPassword");

    assertThat(user.getNickName()).isEqualTo("tester");
    assertThat(user.getEmail()).isEqualTo("test@test.com");
    assertThat(user.getPassword()).isEqualTo("hashedPassword");
    assertThat(user.getRole()).isEqualTo(Role.USER);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"  "})
  void 닉네임이_비어있으면_예외가_발생한다(String nickName) {
    assertThatThrownBy(() -> new User(nickName, "test@test.com",
        "hashedPassword"))
        .isInstanceOf(InvalidOwnerInputException.class);
  }



  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"  "})
  void 이메일이_비어있으면_예외가_발생한다(String email) {
    assertThatThrownBy(() -> new User("tester", email, "hashedPassword"))
        .isInstanceOf(InvalidOwnerInputException.class);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"  "})
  void 비밀번호가_비어있으면_예외가_발생한다(String password) {
    assertThatThrownBy(() -> new User("tester", "test@test.com", password))
        .isInstanceOf(InvalidOwnerInputException.class);
  }
}
