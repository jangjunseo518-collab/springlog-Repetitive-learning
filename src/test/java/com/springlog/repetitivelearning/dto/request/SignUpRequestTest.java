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

class SignUpRequestTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  @DisplayName("정상 요청은 검증을 통과한다.")
  void 정상_요청은_위반이_일어나지_않는다() {
    // given
    SignUpRequest tester = new SignUpRequest(
        "test@test.com", "password123!", "tester");
    Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(tester);

    // when & then
    assertThat(violations).isEmpty();
  }

 @ParameterizedTest
 @NullSource
 @ValueSource(strings = {"", "        ", "1234567", "123456789012345678901"})
 void 유효하지_않은_비밀번호는_위반이_일어난다(String password ) {

   // given
   SignUpRequest tester = new SignUpRequest("test@test.com", password, "tester");
   Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(tester);

   // when & then
   assertThat(violations).isNotEmpty();
 }

  @ParameterizedTest
  @ValueSource(strings = {"12345678", "12345678901234567890"})
  void 유효한_비밀번호는_위반이_일어나지_않는다(String password ) {

    SignUpRequest tester = new SignUpRequest("test@test.com", password, "tester");

    assertThat(validator.validate(tester)).isEmpty();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " ", "not-EmailFormat"})
  void 유효하지않은_이메일은_위반이_일어난다(String email) {

    // given
    SignUpRequest tester = new SignUpRequest(email, "password123!", "tester");
    assertThat(validator.validate(tester)).isNotEmpty();

  }
  
  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", "    "})
  void 유효하지_않은_닉네임은_위반이_일어난다(String nickName) {
    
    // given
    SignUpRequest tester = new SignUpRequest("eamil", "password123!", nickName);
    // when & then
    assertThat(validator.validate(tester)).isNotEmpty();

  }


}