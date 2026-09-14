package com.springlog.repetitivelearning.validation;

import com.springlog.repetitivelearning.dto.request.CreateActivityRequest;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ActivityByCategoryValidator.class)
public @interface ValidActivityByCategory {

  String message() default "필수 필드를 누락 했습니다.";

  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

}
