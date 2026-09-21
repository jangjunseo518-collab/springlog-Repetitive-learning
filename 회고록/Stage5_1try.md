# Stage`5` / `1`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `다수`회 (3번 이하 시 졸업)
* [❌] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- GlobalExceptionHandler를 만들기 전 404, 400 예외를 터트릴 API두개를 추가한다.  
 활동 단건 조회, 활동 생성 두 API 신설. 아직 커스텀 예외가 없으니 임시로 `IllegalArgumentException("임시 예외 처리")` 사용.
 > 활동 전체 조회가 이미 존재하지만, 전체 조회와 단건 조회에 예외 상황시 그 의미가 전혀 다르다.
 > <br>전체 목록 조회가 실패할 경우 단지 빈 리스트를 반환하면 그만이다. 조회 결과가 없어도 예외가 터지지 않는다.
 > <br>반면 단건 조회는 해당 활동에 id를 명확히 지목해 조회했을 떄, 그 결과가 없다면 존재하지 않는 자원을 요청한 것으로 
 > <br>정상 적인 요청이 아닌 예외 상황인 것이다.  
- 커스텀 예외클래스 두개를 신설한다. [ActivityNotFoundException, OwnerNotFoundException]  
 현 시점에 커스텀 예외는 모두 각각 클래스로 만들어 글로벌에서 각각의 핸들러 메서드로 예외를 잡아 처리한다.  
 (추후 다른 스테이지에서 흩어진 커스텀 예외를 하나의 이넘 상수로 관리.)
- GlobalExceptionHandler를 만들어서 커스텀 예외마다 이를 잡는 헨들러 메서드를 구현.  
 예상 못한 예외만을 잡는 헨들러 메서드도 하나 만들어 안정성 확보.
-

## [아쉬운 점]
>이번 stage`5`/ `1`TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] : 커스텀 예외를 만들기 앞서 API를 구현할 때 HTTP 바인딩 애너테이션의 종류별 역할과 용도를 알개 되었다. <br>커스텀 예외클래스를 만들때 `RuntimeException`를 상속받는 이유를 알았고, 핸들러를 만들때 어떤 식으로 부가적인 정보를 넣는지 알게 되었다.
<br>

```angular2html
[ActivityServiceImpl]

public ActivityResponse createActivity(LearningActivity result) {

userRepository.findById(result.getOwner().getId())
.orElseThrow(() -> new IllegalArgumentException("임시 예외 처리"));

LearningActivity activity = new LearningActivity(result.getTitle(), result.getMinutes(),
result.getStudiedOn(), result.getVisibility(),
result.getCategory(), result.getInstructorName(),
result.getCompletionRate(), result.getBookTitle());
activity.assignOwner(result.getOwner());
LearningActivity saved = activityRepository.save(activity);

return ActivityResponse.from(saved);
}
```
- [오류1]: 기껏 요청Dto CreateActivityRequest를 만들었지만 위 생성 메서드에서는 전혀 사용하지 않고 있다.  
 파라미터에 타입을 CreateActivityRequest로 바꿔야 만든 요청 Dto를 활용할 수 있고,  
 컨트롤러에서 `@Valid`를 사용해 Dto에 검증을 실제로 사용할 수 있다. 위 코드로는 검증 없이 사용자가 보낸 요청을 그대로 받게 되는 불안 요소가 있다.

```angular2html
오류 코드 예시.
```
- [오류2]:

```angular2html
[ActivityController]
@GetMapping("/byactivityid")
public ResponseEntity<ActivityResponse> getActivityById(@RequestParam Long activityId) {
```
- [오류3]: 단건 조회에 사용되는 activityId는 /api/activities/5형태의 요청으로 들어오는 경로변수로 받아야한다.  
 그래서 @GetMapping의 경로에 `("/{id}")`를 사용해야하고,  
 메서드의 파라미터에도 경로변수를 받는 어노테이션 `@PathVariable`을 명시 해야만 한다.  
 경로변수에 변수를 쓸때 id를 받는다면 관례적으로 {activityid}처럼 누구의 id인지 명시하는 것이 아닌, {id}로 쓴다.

```angular2html
오류 코드 예시.
```
- [오류4]:



<br><br><br>

> ## [긍정 평가]
> - 내용작성
---



