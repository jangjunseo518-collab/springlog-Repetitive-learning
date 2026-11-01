package com.springlog.repetitivelearning.domain;

import com.springlog.repetitivelearning.domain.type.Role;
import com.springlog.repetitivelearning.exception.InvalidOwnerInputException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "owners")
@Getter
public class User extends BasicEntity {

  @Column(nullable = false, unique = true)
  private String nickName;

  @Column(nullable = false,  unique = true)
  private String email;

  @Column(length = 100)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role = Role.USER;

  @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<LearningActivity> activities = new HashSet<>();

  public User(String nickName, String email, String password) {
    validateNickName(nickName);
    validateEmail(email);
    validatePassword(password);
    this.nickName = nickName.trim();
    this.email = email.trim();
    this.password = password;
  }

  private static void validateNickName(String nickName) {
    if (nickName == null || nickName.isBlank()) {
      throw new InvalidOwnerInputException("닉네임을 입력해 주세요.");
    }
  }
  private static void validateEmail(String email) {
    if (email == null || email.isBlank()) {
      throw new InvalidOwnerInputException("이메일을 입력해 주세요.");
    }
  }

  private static void validatePassword(String password) {
    if (password == null || password.isBlank()) {
      throw new InvalidOwnerInputException("비밀번호를 입력해 주세요");
    }
  }
}
