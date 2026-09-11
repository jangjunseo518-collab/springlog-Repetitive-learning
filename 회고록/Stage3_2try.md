# Stage` ` / ` `TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `준나 많이`회 (3번 이하 시 졸업)
* [❌] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- 이전과 동일 
-
-

## [아쉬운 점]
>이번 stage`3`/ `2`TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] : User 도메인을 만드는 것과 LearningActivity와 관계를 맺는 것 까지는 따라할 수 있으나 접근 제어자, 일관성 등에서 실수가 나타난다.  
### dto를 만드는게 아직 익숙치 않으며 from을 만드는 연습을 더 해야겠다. 
### BasicEntity에 달아둔 라스너가 동작 하려면 `@EnableJpaAuditing`이 어노테이션이 어딘가 있어야하는데 보농 메인 클레스에 달아둔다. <- 이거 안해서 비정상 종료됨 
### service단에서 사용한 메서드 참조식을 조금더 채득화 하자. 
### 결론: 반복 학습을 통해서 각 계층의 구조를 만드는 감을 기르고 구조를 이해하자
<br>

```angular2html
[User]
private void validateNickname(String nickname) {
if (nickname == null || nickname.isBlank()) {   // this. 제거
throw new IllegalArgumentException("닉네임을 입력해주세요.");
}
    }

private static void validateEmail(String email) {
if (email == null || email.isBlank()) {   // this. 제거
throw new IllegalArgumentException("이메일을 입력해 주세요.");
}
    }
```
- [오류1]: User 클래스에서 필드 값 정규화에서 자동완성으로 인한 실수를 인지하지 못 함. 
일관성을 위해 staic을 붙이는게 더 좋을 듯 하다.

```angular2html
[LearningActivity]
private void assingOwner(User owner) {...}

public void assignOwner(User owner) {}
```
- [오류2]: 해당 필드는 Service에서 채워줘야 하는데, 접근 제어자가 private이면 이를 채울 방법이 없어진다.  
그리고 n과g가 뒤집혔다.

그 밖에 오류는 생략한다 이번 try를 기준삼아서 박복 학습을 이어가자.


<br><br><br>

> ## [긍정 평가]
> - 없음. 
---



