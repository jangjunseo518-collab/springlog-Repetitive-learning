package com.springlog.repetitivelearning.validation;


import com.springlog.repetitivelearning.dto.request.CreateActivityRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ActivityByCtagoryValidator implements ConstraintValidator<ValidActivityByType, CreateActivityRequest> {

  @Override
  public boolean isValid(CreateActivityRequest value, ConstraintValidatorContext context) {
    if(value.category() == null) {
      return true;
    }
    return switch (value.category()) {
      case LECTURE -> value.instructorName() != null
                  && !value.instructorName().isBlank();
      case PRACTICE ->  value.completionRate() != null;
      case READING ->  value.bookTitle() != null
                  && !value.bookTitle().isBlank();
    };
  }
}
