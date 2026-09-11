# Stage`3` / `4`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `3`회 (3번 이하 시 졸업)
* [❌] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]
### 클리어 조건은 충족 했으나, 다시 한번 더 실 수 없이, 열람 홧수를 0을 목표로 재시작.

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- 이전과 동일하다.
-
-

## [아쉬운 점]
>이번 stage` `/ ` `TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] :
<br>

```angular2html
[ ActivityRepository]
@Repository
public interface ActivityRepository extends JpaRepository<LearningActivity, Long> {
List<LearningActivity> findByOwnerId(User ownerId);
```
- [오류1]:findByOwnerId(User ownerId);에 파라미터 타입을 Long으로 해야함. User 객체를 가져오는 게 아니니까.

```angular2html
public record ActivityResponse(
Long id,
LocalDateTime createdAt,
LocalDateTime updatedAt,
String title,
int minutes,
Set<String> tags,
  Visibility visibility,
  ActivityCategory category,
  String instructorName,
  Integer completionRate,
  String bookTitle,
  Long ownerId,
  String email <-- 틀린 변수명 nickName으로 수정해야함.
  )
```
- [오류2]: nickName이 오는 자리에 변수 명을 email로 씀 응답으로 온 Json에 email이라는 필드 값에 닉네임이 들어감.  
 실제 nickName 필드의 이름을 email로 dto에 실어서 응답을 보냈기 때문이다.



<br><br><br>

> ## [긍정 평가]
> - 전반적인 구현이 가능했고, 원본 코드를 본 것도 확신이 없었기 때문이고, 사실상 원본 코드를 보고서 고친 부분은 없다.
> - 응답 json을 보고서 Dto에 문제가 있음을 스스로 판단하여 수정함. 
---



