package com.springlog.repetitivelearning.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.Role;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class UserResponseTest {

  @Test
  @DisplayName("User객체를 응답객체로 변환")
  void User객체가_UserResponse로_변환된다() {
    // given
    User user = new User("tester", "test@test.com", "password");
    ReflectionTestUtils.setField(user, "id", 1L);


    // when
    UserResponse userResponse = UserResponse.form(user);

    // then
    assertThat(userResponse.id()).isEqualTo(user.getId());
    assertThat(userResponse.nickName()).isEqualTo(user.getNickName());
    assertThat(userResponse.email()).isEqualTo(user.getEmail());
    assertThat(userResponse.role()).isEqualTo(Role.USER);
  }

  @Test
  @DisplayName("User응답객체에는 비밀번호를 포함하지 않는다")
  void UserResponse에_비밀번호_필드가_없다() {
    // given
    List<String> fildeList = Arrays.stream(
            UserResponse.class.getRecordComponents())
        .map(RecordComponent::getName)
        .toList();

    // when & then
    assertThat(fildeList).doesNotContain("password");


  }

}