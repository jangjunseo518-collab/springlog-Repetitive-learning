# Stage` ` / ` `TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `0`회 (3번 이하 시 졸업)
* [❌] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- 이전과 동일.
-
-

## [아쉬운 점]
>이번 stage` `/ ` `TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] :  로직 구현에 있어 구조적인 이해는 하고있는 것 같지만 여전히 사소하지만 치명적인 실수가 반복된다.  
tag를 정규화하여 앞뒤 공백을 제거하고, 소문자로 변환한 뒤 변수에 할당하는데, 해당 변수를 활용하지 않고 파라미터로 들어온   
tag를 그대로 set에 저장하는 등에 실수가 반복된다. 구조적인 이해는 어느정도 된거 같으니 다음 try에서는 이런 실수를 체크하는 습관을 가자.
<br>

```angular2html
//태그 추가
public void addTag(String tag) {
if(tags.size() >= 10) {// 태그 추가 가능 여부 먼저 확인
throw new IllegalArgumentException("태그는 10개 까지 추가가 가능합니다.");
}
if(tag == null || tag.isBlank()) {//추가 가능하면 유효성 검증 1
throw new IllegalArgumentException("태그를 작성해주세요.");
}
String standardizedTags = tag.trim().toLowerCase();
// 휴효성 검증 후 정규화

if(!tag.matches("^[a-zA-Z가-힣0-9@#-]+$")) {// 정규화 후 유효성 검증
throw new IllegalArgumentException("한글, 영문,숫자,@,-만 입력이 가능합니다");
} <-- 정규화한 값이 아닌 파라미터로 들어온 값을 유효성 검증함.

if(standardizedTags.length() > 20) {//유효한 값이면 글자 수 검증
throw new IllegalArgumentException("태그의 글자 수는 20자 까지 입력 가능합니다.");
}

tags.add(tag); <-- 정규화한 값이 아닌 파라미터로들어온 정규화 전에 값을 저장함.
}

```
- [정규화 한 값 미사용]:뇌빼고 개발하지 말자. 

```angular2html
오류 코드 예시.
```
- [오류2]:



<br><br><br>

> ## [긍정 평가]
> - 로직을 작성하는 구조적 이해나 값 타입 테이블에 관힌 JPA 어노테이션에 대한 이해를 하고있다.
---



