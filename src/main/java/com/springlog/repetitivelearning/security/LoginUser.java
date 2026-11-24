package com.springlog.repetitivelearning.security;

import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.Role;

public record LoginUser(Long id, Role role) {

  public static final LoginUser ANONYMOUS = new LoginUser(null, null);

  public LoginUser{
    if ((id == null) != (role == null)) {
      throw new IllegalArgumentException("id와 role은 둘다 있거나, 들디 없어야 합니다.");
    }
  }

  public static LoginUser from(CustomUserDetails principal) {
    if(principal == null) {
      return ANONYMOUS;
    }

    User user = principal.getUser();
    return new LoginUser(user.getId(), user.getRole());
  }

  public boolean isAnonymous() {
    return id == null;
  }

  public boolean isAdmin() {
    return role == Role.ADMIN;
  }
}