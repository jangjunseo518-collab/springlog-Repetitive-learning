# Stage`4` / `4`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `0`회 (3번 이하 시 졸업)
* [✅] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- LearningActivity클래스에 studiedOn(학습한 날) 팔드를 추가. / 효성 검증 + 생성자에 추가   
 유요성 검증 조건: 학습한 날짜는 선택 사항이니까 null이 올수 있다 -> != null 이면서 오늘보다 미래일 때 예외 발생.
- 요청 dto 구현. 벨리데이션으로 각 요청마다 규칙 적용. 응답 dto에도 studiedOn 추가. 
- 요청dto에서 카테고리별 정규화 검증을 위해 커스텀 어노테이션 구현. 

## [아쉬운 점]
>이번 stage`4`/ `4`TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] : 요청 Dto와 커스텀 어노테이션을 만드는 감이 어느정도 생긴 듯 하나 커스터 어노테이션을 만드는게 아직은 명확하지 않음. <br>Stage4를 처음부터 다시 하기 보다는 커스텀 어노테이선 구현만 다시 하면 좋을 듯 함.

```angular2html
public class ActivityByTypeValidator implements ConstraintValidator<ValidActivityByType, CreateActivityRequest> {

@Override
public boolean isValid(CreateActivityRequest value, ConstraintValidatorContext context) {

if(value.category() == null){
return true;
}

return switch (value.category()) {
case LECTURE -> value.instructorName() != null ? true: null;
}
}
    }
```
- [오류1]: 3항 연산자가 아니라 true를 반환할 조건을 넣어야했음. 아마 등호가 많았던 것만 기억나고 switch의 반환 타입이 boolean이라서   
 true : false를 반환 하게 만든 듯. 반한 타입 자체는 맞지만, 3항 연산자가 아니라 true를 반환하는 조건 null이 아니면서 공백, 빈 문자열이 아닐 경우를 작성해야함.

```angular2html
 private static void validationStudiedOn(LocalDate studiedOn) {
if(studiedOn != null || studiedOn.isAfter(LocalDate.now())) {
throw new IllegalArgumentException("학습한 날짜는 미래일 수 없습니다.");
}
    }
```
- [오류2]: OR 연산자를 사용해서 문젝 되었다. OR연산자의 특징은 앞이 참이면 뒤에 조건은 보지 않고 바로 값을 반환하는 것이다. 
 위 검증은 빈 값이 아닐 때 들어온 값에 유효성을 검증하는 로직이다. 그래서 != null 빈 값이 아닐때를 우선 참으로 두고 그 뒤에 값이 현제 보다 미래라면  
 예외를 터트리 도록 설계했다. 이때 당연히 들어온 값이 널이 아니라면 이미 ||의 앞에서 조건이 참이 되고 예외를 터트림다.  
 그래서 두 조건이 모두 성립해야지 값을 반환하는 && and연산자를 사용해야 한다.  
 빈 값이 아니면서 미래일 때 예외를 터르려야하기 때문이다. 

```angular2html
public class ActivityByTypeValidator implements ConstraintValidator<ValidActivityByType, CreateActivityRequest> {

@Override
public boolean isValid(CreateActivityRequest value, ConstraintValidatorContext context) {

if(value.category() == null){
return true;
}

return switch (value.category()) {
case LECTURE -> value.instructorName() != null
&& !value.instructorName().isBlank()
&& value.instructorName().length() <=50;

case PRACTICE ->  value.completionRate() != null
&& value.completionRate() >= 0 && value.completionRate() <= 100 ;

case READING -> value.bookTitle() != null
&& !value.bookTitle().isBlank()
&& value.bookTitle().length() <= 100;
};
}
    }
```
- [오류3]: 카테고리 별 속성의 정규화를 어노테이션에 전부 몰아 넣으면 어떨까, 더 깔끔하지 않을까 싶었지만,  
 필수 필수 검증과 형식 검증이라는 서로 다른 역할에 검증 로직을 어거지로 &&로 묶어둔 형태가 되었다.  
 이 경우 사용자는 빈 값을 입력하든, 글자수를 잘 적게 혹은 많게 입력하든 결국에는 동일한 예외 메세지를 받게된다.  
 즉, 사용자에게 어떤 원인 때문에 오류가 났는지 알릴 수 있는 수단이 없다. 필수 검증과 형식 검증을 구분하여 관리하자.



<br><br><br>

> ## [긍정 평가]
> - 스스로 발견한 오류의 원인을 정확히 설명해냈어요 (||,&& 차이)
> - 형식 검증과 필수 검증을 분리해야 한다는 설계 원칙을 스스로 도출했어요
> - 실제로 최종 코드에 그 교훈이 반영됐어요
> - 커스텀 검증 3종 세트(어노테이션 정의 → Validator 구현 → DTO 부착)를 다시 스스로 완성했어요
---



