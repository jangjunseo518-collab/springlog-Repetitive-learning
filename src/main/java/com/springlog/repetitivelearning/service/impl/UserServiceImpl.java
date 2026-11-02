package com.springlog.repetitivelearning.service.impl;

import com.springlog.repetitivelearning.domain.User;
import com.springlog.repetitivelearning.dto.request.SignUpRequest;
import com.springlog.repetitivelearning.dto.response.UserResponse;
import com.springlog.repetitivelearning.exception.BusinessException;
import com.springlog.repetitivelearning.exception.ErrorCode;
import com.springlog.repetitivelearning.repository.UserRepository;
import com.springlog.repetitivelearning.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserResponse register(SignUpRequest request) {
    String email = request.email().trim();

    if(userRepository.existsByEmail(email)){
      throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    String encode = passwordEncoder.encode(request.password());
    User saved = userRepository.save(new User(request.nickName(), email, encode));

    log.info("[USER] 회원가입 완료 - id: {}, email: {}", saved.getId(), saved.getEmail());

    return UserResponse.form(saved);
  }
}
