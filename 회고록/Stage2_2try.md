# Stage`2` / `2`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `4`회 (3번 이하 시 졸업)
* [❌] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- 태그 필드를 Set으로 선언하여 중복을 방지한다.  
 값 타입 테이블을 만들도록 `@ElementCollection`을 붙이고 옵션은 (fetch = FetchType.LAZY)로 성정(필요할 때만 조회).  
 `@CollectionTable(name = "activity_tags", joinColumns = @JoinColumn(name = "activity_id"))
`로 값 테이블에 이름과 fk가 들어갈 털럼에 이름을 지정한다.  
 `@C0lumn(name = "tag")`로 tag값이 들어갈 컬럼에 이름을 지정한다.
-
-

## [아쉬운 점]
>이번 stage`2`/ `2`TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] : 아직 갑 타입 테이블에 관한 어노테이션이 익숙하지 않다.   
 읽기 전용으로 반한할 때는 Collections.unmodifiableSet을 사용해야한다.  
 반환 타입이 boolean인 메서드 선언시 헷갈려서 원본 코드를 참고했다. 
  원본 코드 도움 없이 완성하도록 노력하자. 
<br>

```angular2html
오류 코드 예시.
```
- [오류1]:

```angular2html
오류 코드 예시.
```
- [오류2]:



<br><br><br>

> ## [긍정 평가]
> - 저반적으로 실수 없이 잘 만듬.
---



