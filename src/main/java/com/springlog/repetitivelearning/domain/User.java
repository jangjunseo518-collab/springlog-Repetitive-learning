package com.springlog.repetitivelearning.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "user")
@Getter
public class User extends BasicEntity {

  @Column(nullable = false, unique = true)
  private String nickname;
  @Column(nullable = false, unique = true)
  private String email;

  @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
  private List<LearningActivity>  learningActivities = new ArrayList<>();

  public User(String nickname, String email, LearningActivity activity) {
    this.nickname = nickname;
    this.email = email;
  }


  private static void emailVerification(String email) {
    if(email == null || email.isBlank()) {
      throw new IllegalArgumentException("이메일을 작성해주세요.");
    }
  }
  private static void nicknameVerification(String nickname) {
    if(nickname == null || nickname.isBlank()) {
      throw new IllegalArgumentException("닉네임을 적어주세요.");
    }
  }

}
