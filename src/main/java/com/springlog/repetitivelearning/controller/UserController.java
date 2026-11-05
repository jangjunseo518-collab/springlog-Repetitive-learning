package com.springlog.repetitivelearning.controller;

import com.springlog.repetitivelearning.dto.request.SignUpRequest;
import com.springlog.repetitivelearning.dto.response.UserResponse;
import com.springlog.repetitivelearning.service.UserService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserResponse> singUp(
      @Valid @RequestBody SignUpRequest request) {
    UserResponse response = userService.register(request);

    URI location = URI.create("/api/v1/users/" + response.id());
    return ResponseEntity.created(location).body(response);
  }
}
