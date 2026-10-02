# Stage`6-2` / `2`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `0`회 (3번 이하 시 졸업)
* [✅] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- 요구사항 1은 태그로 필터링해야 한다. 즉, 태그와 공개여부를 조건으로 조회하는 파생쿼리 메서드를 새롭게 만들어야한다.
  단일 태그와 공개여부를 파라미터로 받아 사용한다.
- 서비스 레이어 분리.
 현제 ActivityService와 개수 잡계, 전채 태그 정렬등은 성격이 다르기 때문에 별도의 레이어로 분리하여 진행한다.
 태그 조회: List, 카테고리 별 그룹롸: Map, 카테고리 집계: Map, 태그 정렬: Set 으로 인터페이스를 만든다.
- 태그 개수 집계에서는 전체 태그의 개수도 응답에 포함시키기 위해 별도의 응답 dto를 신설한다.
- controller에서는 모두 쿼리스트링으로 받는다.

## [아쉬운 점]
>이번 stage`6-2`/ `2`TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

개수 요약에서 전체 개수를 누락한 상태로 서비스를 구현했고, 응답 dto를 만들고서 정작 사용하지 않았다. 
<br>@RequestParam(required = false)의 의미를 반대로 생각해서 처음에 visibility를 받을때 사용 안 했다.
<br> 동일한 이유로 /all/tags에서 tag를 생략 가능하게 했었다. 필수가 아닌 것에 붙인다는 걸 다시 상기하자.
<br>태그 조회시 trim만 있고 소문자 변환이 없어서 실제 조회시에 불명확한 로직을 만듬. 


### [느낀 점] : 뇌빼고 하지 말고 생각을 하면서 코드를 만들도록 하자. 




<br>

```angular2html
  Map<ActivityCategory, Long> countCategory = new EnumMap<>(ActivityCategory.class);

List<LearningActivity> activities = activityRepository.findByVisibility(
  visibilityPublicValidator(visibility));

  for (LearningActivity activity : activities) {
  countCategory.put(activity.getCategory(), 0L);
  }
...
}  
```
- [오류1]: 사용하지 않은 카테고에 0을 할당하려 했지만 이렇게 코드를 짜버리면 사용중인 카테고리에 0울 할당하게 되어 의미가 없어진다. <br>enum의 values를 사용하자.

```angular2html
오류 코드 예시.
```
- [오류2]:



<br><br><br>

> ## [긍정 평가]
> - 빌드 흐름 자체는 이해한 듯 하다. 
---



