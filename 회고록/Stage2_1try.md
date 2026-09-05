# Stage`2` / `1`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `계속 봄`회 (3번 이하 시 졸업)
* [❌] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- tag는 여러개를 담으니까 List로 할까? -> set으로 하면 중복도 방지할 수 있음
- 태그 뽑는건 getter있으니까 노상관? -> set 사용하니까 set 메서드 사용히자
- API 관점? 서비스,컨트롤러 구현? -> ㄴㄴ 태그 추가,제거,확인 전부 LearningActivity에서.

## [아쉬운 점]
>이번 stage`2`/ `1`TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] : `tag`는 중복을 방지하고 여러개를 객체 하나가 담아야해서 자료구조를 Set으로 하는게 타당했다. 
관계형 DB는 행 하나에 여러 데이터를 넣을 수 없기 때문에 태그 전용 테이블이 필요했다.
@ElementCollection(fetch = FetchType.LAZY)로 해당 필드를 별도의 테이블로 만든다. 
단, 이때 만들어지는 DB의 테이블은 activities테이블과 동일한 테이블이 만들어진다. 
하지만, activities테이블에 PK를 그대로 참조해 FK로 사용한다. 
그래서 활동 객체를 만들고 태그를 추가하면 해당 활동 객체의 PK가 자동으로 테그의 컬럼에 FK로 들어가게된다. 
이는 JPA가 자동으로 해주는 일이기 때문에 개발자가 직접 PK,FK를 각각 매핑할 필요가 없다.  
이렇게 만든 테이블을 프로그래밍 언어 관점에서 값타입 테이블이라고한다.(DB 관점에서 둘다 같은 종류의 테이블임)  
객체로서 사용되지 않고 단순 값만을 저장하는 테이블이기 때문이다.
@CollectionTable(name = "activity_tags", joinColumns = @JoinColumn(name = "activity_id"))  
-> @ElementCollection로 만든 값 타입테이블에 테이블 명과 부모 컬럼(활동객체가 저장되는 activities테이블의 컬럼)의 PK를  
FK로 저장하는 외래키 컬의 이름을 정의한다.  
@Column(name = "tag")은 태그가 저장되는 컬럼의 이름 

<br>

```angular2html
//========== tag 추가
public void addTag(String tag) {
if(tag == null || tag.isBlank()) {
throw new IllegalArgumentException("태그는 비워둘 수 없습니다.");
}
String normalized = tag.trim().toLowerCase();

if(normalized.length() > 20) {
throw new IllegalArgumentException("태그의 길이는 20자 이하만 가능합니다.");
}

if(tags.size() >= 10){
throw new IllegalArgumentException("태그는 10개까지만 추가할 수 있습니다.");
}

if(!normalized.matches("^[a-zA-Z가-힣0-9@#-]+$")) {
throw new IllegalArgumentException("한글,영문,숫자,@,#,-만 입력 가능합니다.");
}

this.tags.add(normalized);
}

//========= tag 제거
public boolean removeTag(String tag) {
if(tag == null || tag.isBlank()) {
return false;
}
return this.tags.remove(tag.trim().toLowerCase());
}

//========== tags 목록을 읽기 전용으로 변한
public Set<String> getTags() {
  return Collections.unmodifiableSet(this.tags);
  }

  //========== tag 등록되어있는지 확인
  public boolean hasTag(String tag) {
  if(tag == null)return false;
  return this.tags.contains(tag.trim().toLowerCase());
  }

```
- [참고]: 위 코드를 보고 이해할 수 있다면, 보지 않고 구현 가능하도록하자.

```angular2html
오류 코드 예시.
```
- [오류2]:



<br><br><br>

> ## [긍정 평가]
> - 없음. 
---



