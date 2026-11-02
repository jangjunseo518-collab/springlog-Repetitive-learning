package com.springlog.repetitivelearning.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
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
    validator =
        Validation.buildDefaultValidatorFactory()
            .getValidator();
  }

  @Test
  @DisplayName("정상 요청은 검증 통과")
  void 정상적인_회원가입_요청은_위반이_일어나지_않는다() {
    // given
    SignUpRequest request = new SignUpRequest(
        "test@test.com", "password123!", "tester");

    // when
    Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

    // then
    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"1234567", "123456789012345678901","","       "})
  void 유효하지_않은_비밀번호는_위반_발생(String password) {

    // given
    SignUpRequest tester = new SignUpRequest(
        "test@test.com", password, "tester");

    // when
    Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(tester);

    // then
    assertThat(violations).isNotEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"12345678", "12345678901234567890"})
  void 올바른_비밀번호는_검증_통과(String password ) {

    // given
    SignUpRequest tester = new SignUpRequest(
        "test@test.com", password, "tester");

    // when
    Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(tester);

    // then
    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " ", "not-an-Email"})
  void 유효하지_않은_이메일은_위반이_발생한다(String email) {
    
    // given
    SignUpRequest tester = new SignUpRequest(
        email, "password123!", "tester");

    // when
    Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(tester);

    // then
    assertThat(violations).isNotEmpty();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", "   "})
  void 유효하지_않은_닉네임은_위반이_발생한다 (String nickName ) {

    // given
    SignUpRequest tester = new SignUpRequest(
        "test@test.com", "password123!", nickName);

    // when
    Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(tester);

    // then
    assertThat(violations).isNotEmpty();

  }

  
}
