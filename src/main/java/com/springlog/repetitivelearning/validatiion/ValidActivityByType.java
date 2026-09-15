package com.springlog.repetitivelearning.validatiion;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ActivityByTypeValidator.class)
public @interface ValidActivityByType {
  String message() default "필수 필드가 누락되었습니다.";

  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

}
