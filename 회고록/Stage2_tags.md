# Stage`2`-tag 구현 / `1`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `0`회 (3번 이하 시 졸업)
* [❌] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- Tag 관련 서비스 구현 
- 테그를 받을 요청 dto를 하나 신설
- 서비스에 addTags등 구현(현시점에 소유권은 검증하지 않는다.)

## [아쉬운 점]
>이번 stage` `/ ` `TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] : Stage2를 구현할 때 구현하지 않은 서비스 로직들을 구현한다.
<br>

```angular2html
public record AddTagsRequest(
@Size(max = 10, message = "태그는 10개 까지 추가할 수 있습니다.")
Set<
@Size(max = 20, message = "태그는 20자를 넘을 수 없습니다.")
@Pattern(regexp = "^[a-zA-Z가-힣0-9@#-]+$",
message = "태그는 한글, 영문, 숫자, @, #, -만 입력 가능합니다.")
String> tags
) {

}
```
- [오류1]: @NotEmpty를 Set위에 추가해서 필수 입력으로 하자. 현제 요청은 테그를 추가하는 요청이니까 테그를 필수로 입력하는게 자연스러움.

```angular2html
 @PostMapping("/{activityId}/tags")
public ResponseEntity<ActivityResponse> addTags(
  @PathVariable Long activityId , @RequestBody @Valid AddTagsRequest request){
  ActivityResponse activity = activityService.addTags(activityId, request);

  return ResponseEntity.status(HttpStatus.Created).body(activity);
  }
```
- [오류2]: 응답 코드가 201 Created인데, 200 OK가 더 자연스럽다. <br> 완전히 새로운 자원(activity 등)을 생성하는 경우엔 201이 맞지만, <br>지금은 이미 존재하는 activity의 필드(태그 컬렉션)에 값을 추가한 것뿐이라 "기존 자원이 갱신되어 그 결과를 돌려주는" 상황이므로 200이 자연스럽다.
  <br>(참고: 201을 쓰려면 새로 생긴 자원을 가리키는 Location 헤더가 필요한데,
  지금은 여러 태그를 한 번에 추가하고 activity 전체를 반환하는 구조라
  가리킬 단일 자원이 없다.)


<br><br><br>

> ## [긍정 평가]
> - 
---



