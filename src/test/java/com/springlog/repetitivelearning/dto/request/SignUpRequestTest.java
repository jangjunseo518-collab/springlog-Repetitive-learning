package com.springlog.repetitivelearning.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import java.util.Set;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class SignUpRequestTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  @DisplayName("정상요청은 검증을 통과한다.")
  void 정상_요청은_검증을_통과한다() {
    // given
    SignUpRequest request = new SignUpRequest(
                 "test@test.com", "password123", "tester");

    // when
    Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

    // then
    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", "        "})   // 빈 문자열, 8칸 공백
  void 비밀번호가_올바르지_않으면_검증_실패(String password) {
    SignUpRequest request = new SignUpRequest("test@test.com", password, "tester");
    Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

    assertThat(validate).isNotEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"12345678", "12345678901234567890"})  // 경계값: 8자, 20자
  void 비밀번호_경계값_8자와_20자는_통과한다(String password) {
    SignUpRequest request = new SignUpRequest("test@test.com", password, "tester");

    assertThat(validator.validate(request)).isEmpty();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", "  ", "not-an-email"})
  void 이메일이_비었거나_형식이_틀리면_검증_실패(String email) {
    SignUpRequest request = new SignUpRequest(email,"password123", "tester");

    assertThat(validator.validate(request)).isNotEmpty();
  }

  @ParameterizedTest
  @NullSource                          // null 한 번
  @ValueSource(strings = {"", "  "})   // 빈 문자열, 공백
  void 닉네임이_올바르지_않으면_검증_실패(String nickName) {
    SignUpRequest request = new SignUpRequest("test@test.com", "password123", nickName);

    assertThat(validator.validate(request)).isNotEmpty();
  }

}
