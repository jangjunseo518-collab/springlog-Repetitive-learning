package com.springlog.repetitivelearning.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Auditable;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "owners")
@Getter
public class User extends BasicEntity{

  @Column(nullable = false,  unique = true)
  private String nickname;
  @Column(nullable = false,  unique = true)
  private String email;

  @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL,  orphanRemoval = true)
  private Set<LearningActivity> activities = new HashSet<>();

  public User(String nickname, String email) {
    validateNickname(nickname);
    validateEmail(email);
    this.nickname = nickname.trim();
    this.email = email.trim();
  }

  // 유효성 검증
  private static void validateNickname(String nickname) {
    if (nickname == null || nickname.isBlank()) {
      throw new IllegalArgumentException("닉네임을 입력해주세요.");
    }
  }
 private static void validateEmail(String email) {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("이메일을 입력해 주세요.");
    }
 }

}

