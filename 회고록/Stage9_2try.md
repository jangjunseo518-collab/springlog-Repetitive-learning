# Stage`9` / `2`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `0`회 (3번 이하 시 졸업)
* [❌] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- 이력을 담을 엔티티 클래스 생성/기존 엔티티와 별개로 별도 테이블로 관리.
  무슨 작업이었는지 + 상세 설명 + 활동의 주인을 담아야하는데 어떤 작업인지는 따로 이넘으로 관리.
- 이력감사 전용 Repository 신설 
 이때 정렬 조건과 정렬 방향은 파생 쿼리 메서드 명에 명시한다.(ID,내림차순[최근 순 조회가 요구사항임.])
- 응답 Dto 신설1: 파생쿼리 메서드로 반환되는 엔티티 타입을 dto로 변환해 필요한 정보만 선택적으로 담는다.
- 응답 Dto 신설2: 위에서 만든 응답 Dto는 레포지토리에서 조회한 Page의 제네릭 타입을 변환하는 과정이라면, 
  이번 Dto의 경우에는 1번 Dto가 반든 page객체와 클램핑 과정에서 반환되는 조정 메세지를 담는 최종 응답 Dto이다.
- 기본 ActivityServiceImpl 생성,수정,삭제 메서드에서 이력 객체 생성 후 저장 로직 추가. 
  모든 이력 객체에 공통으로 들어가는 title, 활동id, category, ownerId를 템플릿으로 고정하는 헬퍼 메서드를 만든다.
- 이력 서비스 시설 후 컨트롤러 신설

## [아쉬운 점]
>이번 stage`9`/ `2`TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] :
<br>

```angular2html
@Entity
@Table(name = "activity_audit_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ActivityAuditLog extends BasicEntity {...}
```
- [오류1]: Getter를 안 달았음. 

```angular2html
오류 코드 예시.
```
- [오류2]:



<br><br><br>

> ## [긍정 평가]
> - 내용작성
---



