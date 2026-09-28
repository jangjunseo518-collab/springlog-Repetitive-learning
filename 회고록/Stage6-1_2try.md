# Stage`6-1` / `2`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `0`회 (3번 이하 시 졸업)
* [❌] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
-[페이징]
- 리포지토리에 페이징 조회 파생쿼리 작성 (page<T>, Slice<T>)
<br> JPA 파생쿼리 규칙에 따라 findBy 뒤에 Pageable은 제외하고 Visibility만 붙인다.
- Paging요청 Dto 구현: Page, Slice 둘다 동일한 요청Dto를 사용한다. 
- Page<T>,Slice<T>의 응답 Dto를 만든다. null필드를 응답에 담지 않기 위해 <br>@JsonInclude(JsonInclude.Include.NON_NULL) 를 사용한다. pageSize애 값이 조정되었을 시 반환되는 메세지를 담을 필드가 필요히디. <br> 정적펙터리를 구현한다.
<br> Slice<T>의 응답 Dto는 다음 요소의 유무를 담는 필드도 추가한다. 
- 공개여부를 검증하는 헬퍼메서드를 유틸리티 클래스로 구현. null-> PUBLIC/ != PUBLIC -> 커스텀 예외 
- service 인터페이스에 시그니처추가.
- pageSize를 클램핑한 값과 조정했다는 문자열을 담을 record를 만든다. 
- pageSize를 클램핑할 헬퍼 메서드를 유틸리티 클래스로 구현. private  final static int으로 MAX,MIN,DEFAULT 상수를 선언. 
- 페이징에 필요한 값들에 계산이 끝난 뒤 모든 값을 하나로 담을 record 구현. PagingSetup. <br> 정규화한 공개여부, PageSize 클럄핑한 메세지를 담는 필드, pageable(페이지,페이지사이즈,정렬기준,방향)필드.
- 모든 재료를 담을 그릇 PagingSetup을 만들었으니 이 그릇을 채워줄 헬퍼 메서드가 필요하다. buildPagingSetup
  공개여부, pageSize, 정렬 방향, page(일단 음수 허용) 정규화 후, page에 음수가 할당된었다면 커스텀 예외.
<br> switch표현식을 이용해서 sort의 값을 정규화한다. 이때 기준 외 값이라면 커스텀 예외.
<br> 정규화한 정렬바향과 정렬기준으로 Sort.by -> Sort객체 생성, 정규화한 page, pageSizeResult안에 pageSize, 방금 만든 sort로 pageble 객체 생성.
<br> 가장 처음 정규화한 공개여부, pageSizeResult안에 클램핑 메세지, pageable로 PagingSetup을 생성.
- 이렇게 만든 buildPagingSetup을 서비스에서 호출하여 DB를 조회할때 사용하는 PagingSetup을 반환받아 사용. 
- 각각 레포지토리에서 Page<ActivityResponse>,Slice<ActivityResponse>를 반환 받고, 제네릭 타입을 ActivityResponse로 변환.(Slice의 경우 hasNext를 뽑아 변수에 할당.)
- 이후 각각 PageResponse,SliceResponse를 반환 한다. 
- Controller 구현은 파라미터를 받는 바인딩하는 방식만 주의하자. @ModelAttribute
- 이 시점에서  @ModelAttribute로 바인딩 실패 예외가 살짝 보기에 안 좋게 나오기 때문에 핸들러를 리펙터링한다. 
<br> 예외가 터진 원인이 TypeMismatchException인지를 판단해 TypeMismatchException라면 필드와 시용자 입력값, 올바른 입력값 목록을 반환해 메세지로 조립해 반환. 
- [단순 조회] 
- SearchRequest 요청 dto를 구현
## [아쉬운 점]
>이번 stage` `/ ` `TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] :
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
> - 내용작성
---



