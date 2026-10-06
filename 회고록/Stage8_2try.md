# Stage`8` / `2`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `0`회 (3번 이하 시 졸업)
* [❌] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- yaml설정 
- dev에 로컬 업로드 파일 경로 추가 
- 인터페이스 추가
- 커스텀 예외 추가: 생성자 둘. 하나는 메세지만, 다른 하나는 메세지와 예외 객체. 헨들러는 만들지 않는다.<br> 500 에러이고, Exception.class의 헨들러handleException가 이를 처리한다. 
- IllegalArgumentException를 잡는 헨들러 신설.
- FileStorage의 구현체 생성. 필드는 허용하는 확장자를 갖고있는 Set과 경로를 갖는 Path를 갖는다.
- 기존 서비스 리페터링: 우선 엔티티 클래스에 파일 명을 담을 수 있는 문자열 필드를 추가한다. 
<br> 서비스 리펙터링(create, deleteActivity 등), 저장된 파일명을 반환하는 헬퍼 메서드 구현.
- 컨트롤러 구현

## [아쉬운 점]
>이번 stage`8`/ `2`TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] : 노션에 꽤나 의존해서 빌드했다. 다음 트라이는 코드 보다는 줄글을 보고 글을 보기 전, 무엇이 필요한지 요구사항을 보고서 먼저 떠올려본 뒤, 구현 방법을 모르는 걸 찾아보자.
<br>

```angular2html
[LocalFileStorage]
private final Path uploadPath;

public LocalFileStorage(
@Value("${sprintlog.file-directory}") String uploadPathByYam) {
this.uploadPath = Paths.get(uploadPathByYam)
.toAbsolutePath().normalize();
try {
Files.createDirectories(uploadPath);
log.info("업로드 디렉터리 준비 완료: {}", uploadPath);
}catch (IOException e) {
throw new FileStorageException("업로드 디렉토리 생성 실패: " +  uploadPathByYam);
}
    }
```
- [오류1]: 생성자에서 예와가 발생할 때 IOException 예외 객체 e를 사용 안 하고있음 이러면 causeBy:가 로그에 안 생김.
- throw new FileStorageException("업로드 디렉토리 생성 실패: " +  uploadPathByYam, `e` );를 추가해서 예외 객체를 활용하자.

```angular2html
오류 코드 예시.
```
- [오류2]:



<br><br><br>

> ## [긍정 평가]
> - 노션을 참고하긴 했지만, 무엇이 왜 필요하고 어떤 역할을 하는지 이해할 수 있는 것을 확인함.
---



