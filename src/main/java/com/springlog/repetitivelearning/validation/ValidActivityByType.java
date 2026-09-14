package com.springlog.repetitivelearning.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)//TYPE옵션-> 클래스 선언부에 부착가능.
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ActivityByCtagoryValidator.class)
public @interface ValidActivityByType {

  String message() default "카테고리에 필수 필드가 누락 되었습니다.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
