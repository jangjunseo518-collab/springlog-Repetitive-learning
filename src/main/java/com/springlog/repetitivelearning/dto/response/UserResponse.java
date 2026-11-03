package com.springlog.repetitivelearning.dto.response;

import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.Role;

public record UserResponse(

    Long id,
    String email,
    String nickName,
    Role role
) {

  public static  UserResponse from(User user) {
    return new UserResponse(
        user.getId(),
        user.getEmail(),
        user.getNickName(),
        user.getRole());
  }

}
