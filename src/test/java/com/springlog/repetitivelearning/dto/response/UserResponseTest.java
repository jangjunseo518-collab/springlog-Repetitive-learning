package com.springlog.repetitivelearning.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.Role;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class UserResponseTest {

  @Test
  @DisplayName("User객체를 UserResponse로 변환")
  void User를_UserResponse로_변환한다() {
    // given
    User user = new User("tester", "test@test.com", "password123!");
    ReflectionTestUtils.setField(user, "id", 1L);

    // when
    UserResponse userResponse = UserResponse.from(user);

    // then
    assertThat(userResponse.id()).isEqualTo(user.getId());
    assertThat(userResponse.email()).isEqualTo(user.getEmail());
    assertThat(userResponse.nickName()).isEqualTo(user.getNickName());
    assertThat(userResponse.role()).isEqualTo(Role.USER);
  }

  @Test
  @DisplayName("응답에는 비밀번호가 포함되지 않는다")
  void 응답에는_비밀번호가_포함되지_않는다() {
    // given
    List<String> filedNames = Arrays.stream(UserResponse.class.getRecordComponents())
        .map(RecordComponent::getName)
        .toList();

    // when & then
    assertThat(filedNames).doesNotContain("password");



  }

}