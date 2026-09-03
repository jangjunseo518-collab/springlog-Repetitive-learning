# 스스로 생각한 진행 순서
> 학습활동 세가지를 카테고로리 분류하여 enum클래스를 만든다.  
> 공개여부도 enum클래스로 만든다.  
> 생성,수정 시간과 Id는 BasicEntity클래스를 만들어 자동으로 채우며 Entity클래스가 상속하도록한다.  
> LearningActivity는 BasicEntity를 상속한다  
> 생성자를 만들고 제목과 학습 시간은 사용자가 입력하는 값이기 때문에 두 값이 유효한지 검증하는 메서드를 생성자 가장 위에서 호출한다.  
> String 타입 title은 null체크를 해야하지만 int는 null을 받을 수 없으니까 minutes는 null체크를 하지 않는다.  
> title은 .trim()을 붙여 생성자 안에서 값이 필드에 할당 될때 좌우 공백을 지운다. (필수는 아니지만 깔끔한 데이터를 위해.)  
> enum 타입 필드는 선택지인 상수 내에서만 값을 고룰 수 있어서 굳이 유효성 검증을 하지 않는다  
> 


## Enum
> 현제는 단지 상수로 선언 헀을 뿐 아무런 로직을 갖고있지 않다.


---
# 📝 학습 내용

## `BasicEntity`
`@MappedSuperclass`를 선언부에 붙여서 JPA가 인식하도록하며 테이블로 만들지 않게 했다.  
&nbsp; &nbsp;&nbsp;&nbsp; 다만, 리스너 어노테이션이 필요한건 알지만  이름을 까먹음. 

> @EntityListener(AuditingEntityListener.class) 그리고 @Getter도 까먹고 안 달았음.
 
## 클래스 본문 어노테이션.  
### 어노테이션 이름을 기억 못함.
- @Id : JPA가 해당 필드를 컬럼으로 만들때 해당 컬럼을 pk(기본키)로 만들라는 표시.
-  @GeneratedValue(strategy = GenerationType.IDENTITY)
- @Column(nullable = false): 테이블을 만들때 해당 컬럼은 null이 올수 없음 NOT NULL 제약 조건을 붙여서 컬럼을 만든다.
> [@GeneratedValue]: JPA가 pk를 자동으로 만들도록 하는 어노테이션이고, strategy = ...은 어떤 전략으로 pk를 자동 생성할지를 결정하는 옵션이다.
> GenerationType.IDENTITY는 1부터 순차적으로 pk를 만드는 전략이다.  
> 추가로 pk는 테이블 자체에 번호가 아니라 테이블에 속한 행을 구분짓는 고유번호로, 서로 다른 테이블에는 아무런 영향을 주지 않는다.  
> 즉, 사용자 테블에 pk1인 행과 활동 테이블에 pk1인 행이 동시에 존재한다는 뜻이다. 

## LearningActivity
클래스 선언부의 `@Table(name = "activities")`과 enum타입 필드에  `@Enumerated(EnumType.STRING)`를 까먹고 안 붙임.  
나머지 어노테이션은 잘 붙임.

```
강사이름 정규화
  private void instructorNameNormalization(ActivityCategory category , String instructorName) {
    if(category == ActivityCategory.LECTURE && (instructorName == null || instructorName.isBlank())) {
      return "강사 미정";
    }
    return instructorName;
  }

반한 타입을 void로 선언하여 컴파일 오류가 났다. 반환 타입은 String이다.
추가로 클래스 인스턴스(객체)의 상태에 읜존하지 않기 때문에 static을 붙일 수 있지만 
```

## 사소하지만 치명적인 실수

1. 심각한 버그: validateMinutes 로직이 완전히 뒤집혀있다.
```
private static void validateMinutes(int minutes) {
if(minutes > 1) {
throw new IllegalArgumentException("학습 시간은 1분 이상만 입력이 가능합니다.");
   }
}
if(minutes > 1) -> if(minutes < 1) 수정.
```
2. 설계 문제: 상태변경 메서드들이 전부 private으로 되어있다.
```
private void changeToPublic() { ... }
private void changeToPrivate() { ... }
private void changeTitle(String newTitle) { ... }
private void increaseInStudyTime(int increaseMinutes) { ... }

private -> public
```
뇌빼고 만들지 말고, 생각을 하면서 만들도록 하자.

---

# 원본 코드를 참고해 개선한 것.
## static 메서드로 표현 가능한 메서드를 전부 static으로 선언하여 코드의 의미를 명확히 했다.
> `Static 메서드`: 객체의 상태에 의존하지않고 단지 들어온 데이터를 가지고서 어떤 정해진 값을 반환하는 순수 함수(pure function)  
> `판단 기준`: "이 메서드가 this(자기 클래스의 필드)를 전혀 안 쓰고, 순수하게 파라미터만으로 결과를 만들어내는가?" → 그렇다면 static을 붙이는 게 의미적으로 더 정확한 표현이다.


### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `4`회 (3번 이하 시 졸업)
* [❌] 다음 stage로 넘어가도 되는가? [✅ ❌ 선택]