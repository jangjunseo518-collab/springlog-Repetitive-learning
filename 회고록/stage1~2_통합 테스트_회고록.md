# Stage`1~2` / `1`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `0`회 (3번 이하 시 졸업)
* [❌] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- stage1~2와 동일.
-
-

## [아쉬운 점]
>이번 stage`1~2`/ `1`TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] : 여전히 자잘한 실수 때문에 심각한 오류가 발생한다. 
<br>

```angular2html
if(standardizedTag.matches("^[a-zA-Z가-힣0-9@#-]+$")){
throw new IllegalArgumentException("태그는 한글,영문,숫자,#,@,-만 작성 가능합니다.");
}
```
- [심각한 버그: matches 조건이 뒤집힘]:  !를 안 써서 정규화 규칙이 반대로 뒤집혔다.

```angular2html
// 태그 제거
public void removeTag(String tag) {
if(tag == null || tag.isBlank()) {
return;
}
tags.remove(tag.trim().toLowerCase());
}
```
- [반환 타입이 void]: 삭제 기능 자체에 문제는 없지만, 실제 삭제를 실행 했을때 그 결과를 반환하지 않아서 지워졌는지 아닌지를 알 수 없다.  
 이 경우 removeTag를 호출한 호출부(서비스단)에서 추가로 tags에 방금 삭제한 태그가 남아있는지 없는지를 알아보는 메서드를 넣어야한다.

```angular2html
public void changeMinutes(int minutes) {
validateMinutes(minutes);
this.minutes = minutes;
}
```
- [값 덮어쓰기]: 관점에 따라서 실수로 넣은 학습시간을 줄이거나 변경하는 기능을 넣는 것도 좋을 듯 하나,   
요구사항에는 값을 더하라고 명시되어있다. 그리고 값을 덮어 쓴다면 별도의 앤드포인트로 따로 메서드를 더하기,빼기,덮어쓰기로 나누는게 낫지 않을까 고민해보는 것도 좋을 듯 하다.



<br><br><br>

> ## [긍정 평가]
> - 전반적인 로직에 구조는 이해한 듯. 자잘한 실수 때문에 심각한 오류를 내는 것들만 더 신경 쓰자. 
---



