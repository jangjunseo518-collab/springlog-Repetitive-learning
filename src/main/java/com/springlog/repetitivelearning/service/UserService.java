package com.springlog.repetitivelearning.service;

import com.springlog.repetitivelearning.dto.request.SignUpRequest;
import com.springlog.repetitivelearning.dto.response.UserResponse;

public interface UserService {

  UserResponse register(SignUpRequest request);

}
