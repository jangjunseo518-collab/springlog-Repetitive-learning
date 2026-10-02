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
>이번 stage` `/ ` `TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] :
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
> - 내용작성
---



