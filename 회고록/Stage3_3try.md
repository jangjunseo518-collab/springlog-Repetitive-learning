# Stage`3` / `3`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `다수`회 (3번 이하 시 졸업)
* [❌] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- User 도매인 설계 
- 응답 Dto
- Repository 

## [아쉬운 점]
>이번 stage`3`/ `3`TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] : 느낌 이랄까 깨달은 점, 
```angular2html
[LearningActivity]
//owner
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "owner_id")
private User owner;
...
//owner 필드 할당
  public void assignOwner(User owner) {
    if (owner == null) {
      throw new IllegalArgumentException("사용자를 입력해주세요.");
    }
    this.owner = owner;
  }
```
### 이 필드에 할당되는 건 owner 객체인데 어떻게 activities테이블에 owner_id FK컬럼에 owner의 PK가 할당이 되는지 궁굼했는데, <br>오너 객체 생성시 오너 클래스가 상속한 베이직 엔티티에서 자동으로 id (PK)컬럼과 생성 수정 컬럼을 만들어주고, assignOwner를 통해서 <br>필드에 객체가 할당되면, @ManyToOne+@JoinColumn를 보고서 Hibernate가 그 User의 id 값을 꺼내서 activities.owner_id라는 별개의 컬럼에 넣어준다.
### DataInitializer를 만드는 것도 연습하자. 

<br>

```angular2html
@OneToMany(mappedBy = "owner",cascade = CascadeType.ALL,  orphanRemoval = true)
@JoinColumn(name = "")
private Set<LearningActivity> activities = new HashSet<>();

```
- [오류1]:`@JoinColumn(name = "")`은 User 클래스에 Set<LearningActivity> activities필드가 아니라,  
LearningActivity 클래스에 owner 필드에 붙여야한다. 이걸 헷 갈려서 여기 붙임.

```angular2html
[ActivityResponse]
public ActivityResponse activities(LearningActivity activity) {
User owner = activity.getOwner();
Long ownerId = (owner != null) ? owner.getId() : null;
String ownerNickname = (owner != null) ? owner.getNickName() : null; <- 이 줄은 기억 못함 
```
- [오류2]: static이 빠져서 정적 펙토리를 사용하려면 Dto 객체를 불러야한다. static을 붙여서 어디서든 이 정적 펙토리를 사용할 수 있게 하자.  
 그리고 메서드 이름이 activities로 되어 있는데 기능에 문제는 없지만, 의미상 부적절하다. from을 메서드 이름으로 하는게 의미적으로 정확하며,  
 개발 과정중에 더 직관적으로 활용이 가능하다.  
  String ownerNickname = (owner != null) ? owner.getNickName() : null;-> 닉네임을 가져와야 응답을 만들어 낼 수 있다.  
 응답에서 owner에 필드 중 필요한 정보는 id와 nickName이란 걸 기억하자.

```angular2html
[ActivityServiceImpl]


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) <- 여기에 트랜젝션을 걸어두면 메서드에 안 걸어도 됨(업데이트 저장에는 따로 기본 트렌젝션을 걸어줘야함)
public class ActivityServiceImpl implements ActivityService {
...
@Override
public List<ActivityResponse> activitiesByOwnerId(Long ownerId) {
  List<LearningActivity> activities = activityRepository.findByOwnerId(ownerId);
  return activities.stream()
      .map(ActivityResponse :: from).toList();
  }
}
```
- [오류2]: @Transactional(readOnly = true)을 빼먹음. 읽기 전용으로 안 걸어면  
불필요한 변경 감지 동작을 하이버네이트가 시도함 성능 저하로 이어짐 그래서 조회 로직은 읽기 전용 명시. 클래스 선언부에 읽기 전용을 걸고,  
나중에 업데이트, 저장 로직에 기본 트랜젝션을 따로 달아주는게 더 안전한 설계임.

```
[ActivityController]
  @GetMapping
  public ResponseEntity<List<ActivityResponse>> getAllActivities(@RequestParam Long onerId) {
    List<ActivityResponse> activities = activityService.activitiesByOwnerId(onerId);
    List<ActivityResponse> list = activities.stream()
        .map(ActivityResponse::from).toList(); 
  return ResponseEntity.status(HttpStatus.OK).body(list);
  }

```
- [오류]: 응답Dto로 변환하는 책임은 서비스 단에서 가지고있고, 서비스 단에서 이미 변환 했지만, 그걸 까먹고 컨트롤러의 메서드 안에서   
이미 응답Dto로 변환한 List를 다시 응답Dto로 변환 하려고해서 컴파일 오류 남. 응답Dto로 변환하는 책임은 서비스 단이란 걸 잊지말자.

<br><br><br>

> ## [긍정 평가]
> - 구조적인 이해는 어느정도 완료된 듯 하나 디테일 적인 요소가 많이 누락되었다.
> - 계층간 분리, 클래스 내부 구조를 구현하는 것 까지는 가능함.
> - 어노태이션이나, 계층별 책임이나, 정적 펙토리얼을 static으로 선언 + 메서드 이름from으로 명확한 의미 전달 등 
    디테일적인 걸 더 산경쓰자.
---



