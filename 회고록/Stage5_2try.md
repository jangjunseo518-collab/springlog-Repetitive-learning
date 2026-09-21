# Stage`5` / `2`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `0`회 (3번 이하 시 졸업)
* [✅] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- 이전과 동일
- 
-

## [아쉬운 점]
>이번 stage` `/ ` `TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] : 예외 계층을 만들고 핸들러로 각 예외마다 알맞은 응답을 만드는 감을 익힌 것 같다. 다만, 아직 익숙하지 않아서 한번더 만들어보고 다음으로 넘어간다. 
<br>

```angular2html
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidation(MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();

    e.getBindingResult().getAllErrors().forEach((error) -> {
    errors.put(error.getDefaultMessage(), error.getCode()); 
  });
...
}
```
- [오류1]: 우선 맵을 채우는 과정에서 `getBindingResult()`를 통해 `getAllErrors()`를 호출한다. getAllErrors는 List<ObjectError>를 리턴하고, ObjectError는 FieldError의 부모 클래스이며, <br> 필드 단위의 감증 실패 오류 뿐만 아니라, 클래스 단위의 전역 검증 실패까지 전부 가지고있다. <br>여기서 문제는 ObjectError는 `getField()`같은 메서드를 갖고있지 않기 때문에 현제 상태로는 어떤 필터에서 감증 실패했는지를 알 방법이 없다.  
 그리고 `put(error.getDefaultMessage(), error.getCode());`도 메세지와 필드의 순서가 반대로 들어갔다. (gteCode는 getField로 변경해야함 ) 
> [알맞은 형태 ]
> <br> e.getBindingResult().getFieldErrors().forEach((error) -> {
 <br>errors.put(error.getField(), error.getDefaultMessage());
 <br>});

```angular2html
오류 코드 예시.
```
- [오류2]:



<br><br><br>

> ## [긍정 평가]
> - 예외 파라미터를 String message에서 Long id로 바꿔서, 메시지 조립 책임을 예외 클래스 자신에게 캡슐화한 것 — 원본 방향과 정확히 일치
> -  ActivityController의 세 엔드포인트(@PathVariable, @RequestParam, @RequestBody)를 각 역할에 맞게 정확히 구분해서 적용한 것
> -  ActivityServiceImpl에서 파라미터 이름을 activityId로 명확하게 정리해서, 이전에 있었던 "ownerId인지 activityId인지" 헷갈리던 문제를 스스로 잡아낸 것
> -  GlobalExceptionHandler도 404(단건 없음), 400(검증 실패), 500(예상 못한 예외) 세 가지를 원본과 거의 동일한 구조로 잘 구현했고, getFieldErrors() 관련 실수도 스스로 다시 짚어서 바로잡은 것
---



