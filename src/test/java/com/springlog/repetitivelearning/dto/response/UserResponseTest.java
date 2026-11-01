package com.springlog.repetitivelearning.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.Role;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserResponseTest {

  @Test
  @DisplayName("User를 UserResponse로 변환")
  void User를_UserResponse로_변환한다() {
    // given
    User user = new User("tester", "test@test.com", "hashedPassword");

    // when
    UserResponse response = UserResponse.from(user);

    // then
    assertThat(response.email()).isEqualTo("test@test.com");
    assertThat(response.nickName()).isEqualTo("tester");
    assertThat(response.role()).isEqualTo(Role.USER);
  }

  @Test
  @DisplayName("응답에는 비밀번호 필드를 포함하지 않는다.")
  void 응답에_비밀번호_필드를_포함하지_않는다 () {
    // given
    List<String> fieldNames = Arrays.stream(UserResponse.class.getRecordComponents())
        .map(RecordComponent::getName)
        .toList();

    // when & then
    assertThat(fieldNames).doesNotContain("password");

  }


}
