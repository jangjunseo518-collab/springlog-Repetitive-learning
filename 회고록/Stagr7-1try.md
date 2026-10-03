# Stage`7` / `1`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `1`회 (3번 이하 시 졸업)
* [❌] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]
* 확인차 클로드에세 로직 검사를 다수 받아서 한번더 혼자 구현하고 넘어가자.

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- 우선 로케이션 헤더(위치 정보)를 추가하기 위해서 ActivityController에 createActivity 매서드를 리페터링한다.
  <br> URI location = URI.create("/api/activities/" + activity.id());의 형태로 만들 수 있다. 응답에는 Http를 만드는 부분을 지우고 <br>ResponseEntity.created(location).body(activity);로 반환한다.
- 서비스 로직 구현 [본문으로 보내는 값이 여러개이거나, Valid 검증이 필요하다면 Dto 신설 ]
  멱등성이 있는 요청은  @PatchMapping[title,visibility]/ 멱등성이 없는 요청은 @PostMapping [minutes]
-

## [아쉬운 점]
>이번 stage`7`/ `1`TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] : 로직 구현 자체는 문제 없지만 dto 적용 기준이나 요청을 @PostMapping/@PatchMapping 중 어느것으로 할지를 많이 헷가렸다.
<br>

```angular2html
package com.springlog.repetitivelearning.exception;

import com.springlog.repetitivelearning.domain.type.Visibility;

public class SameVisibilityException extends RuntimeException {

public SameVisibilityException(Visibility visibility) {
super("공개여부가 동일합니다.");
  }
}

```
- [오류1]: 공개 여부를 변경할 때 동일한 공개 여부로 변경시 예외를 터트리려 했으나 조용히 아무 일도 안 일어나는게 더 실무적인 설계라는 것을 알게됨

```angular2html
ActivityResponse changeTitke(Long activityId, String newTitle);
ActivityResponse changeToPublic(Long activityId);
ActivityResponse changeToPrivate(Long activityId);
ActivityResponse increaseMinutes(Long activityId, int minutes);
```
- [오류2]: 본문으로 보내는 값이 여러 개이거나, @Valid 검증을 걸고 싶은 값이 있으면 DTO를 만들어야 하지만 만들지 않고서 서비스를 구현했다.



<br><br><br>

> ## [긍정 평가]
> - 내용작성
---



