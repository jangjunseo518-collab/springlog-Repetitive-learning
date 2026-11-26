package com.springlog.repetitivelearning.security;

import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.domain.type.Role;

public record LoginUser (Long userId, Role role){

  public static final LoginUser ANONYMOUS = new LoginUser(null, null);

  public LoginUser{
    if((userId == null) != (role == null) ){
      throw new IllegalArgumentException("id와 role에 값은 둘다 없거나, 둘다 있어야 합니다.");
    }
  }

  public static LoginUser from(CustomUserDetails principal) {
    if(principal == null){
      return ANONYMOUS;
    }

    User user = principal.getUser();

    return new LoginUser(user.getId(), user.getRole());
  }



  public boolean isAnonymous() {
    return userId == null;
  }

  public boolean isAdmin(){
    return role == Role.ADMIN;
  }

}
