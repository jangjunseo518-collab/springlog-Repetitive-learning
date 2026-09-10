# Stage`3` / `1`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `겁나 많이`회 (3번 이하 시 졸업)
* [❌] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- User 클래스를 만든다.
- 응답 Dto를 만든다. + 정적 펙터리를 만듬. 
- repository 만듬. 인터페이스로 만들고 JpaRepository를 상속
- 서비스 인터페이스를 만들고 구현체를 따로 만든다.

## [아쉬운 점]
>이번 stage`3`/ `1`TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] : 1:N 관계부터 DTO, 서비스단, 레퍼지토리, 컨트롤러가 한번에 시작되는 단계라서 각각의 계발 패턴을 익혀야 할 거 같다.  
특히 클래스 명을 만드는 관례나 의미를 명확히 하는 방법을 더 연습해야할 거 같다.
Dto 만들 때 활동과 유저에 정보를 모두 담는 응답을 만드는 Dto인데, userDto라고 클래스를 만들었다.  
활동의 제목,테그, 생성/변경 시간 등 모든 필드와 누구의 활동인지 까지 담아야하는데, Dto의 필드를 id랑 nickName만 담믄 실수를 했다.  
그 외에도 실제 코드는 활동 activities 테이블을 다루지만, userRepository라고 클래스 명을 만드는 등의 실수를 했다.
<br>

```angular2html
// LearningActivity.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "owner_id")
private User user;     // ← 필드 이름이 "user"

// User.java
@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
private List<LearningActivity> activities = new ArrayList<>();
  //                    ↑ "owner"라는 필드를 찾으라는데, LearningActivity엔 "owner" 필드가 없음(user)
```
- [오류1]:하이버네이트가 mappedBy = "owner"를 보고 owner필드를 찾는데, 정작 LearningActivity에서 필드 명을 user로 만듬.

```angular2html
public void addTag(String tag) {
String standardizedTag = tag.trim().toLowerCase(); // ← tag가 null이면 여기서 바로 NPE

if(tags.size() >= 10) { ... }
if(tag == null || tag.isBlank()) { ... }  // 이미 늦음 — 위에서 이미 터짐
...
}
```
- [오류2]: NPE를 잡는 코드가 아래 있다. 정규화 과정에서 이미 NPE가 터질 위험이 있다.  
  
```
public void addTag (String tag) { 에 이미 있는 태그 추가시가 빠져있음
   ...
   if (tags.contains(standardizedTag)) {
   return;
   } <- 추가 
   ...  
 }
```
- [오류3]: 이미 태그가 10개인 상태에서 중복되는 태그를 추가하면, 사실상 중복 불허용으로 아무런 일도 안 일어나는데,  
부당하게 예외 메세지가 터진다. 

DTO,서비스,레포지토리,컨트롤러 등 오류가 많지만 생략한다. 

<br><br><br>

> ## [긍정 평가]
> - 없음.
---



