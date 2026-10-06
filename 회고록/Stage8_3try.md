# Stage`8` / `3`TRY

---
### 🎓 졸업 판정
* **원본 코드 열람 횟수:** `0`회 (3번 이하 시 졸업)
* [✅] 다음 stage로 넘어가도 되는가?  [✅,❌ 선택]

### 📝 학습 내용

## [설계 의도와 빌드 순서]
- yaml 설정
- FileStorage 인터페이스 
- 커스텀 예외 추가. <- 500, 헨들러는 만들지 않는다 /  IllegalArgumentException 헨들러 추가 <- 400 처리함.
- 구현체 
- 서비스 
- 컨트롤러 

## [아쉬운 점]
>이번 stage`8`/ `3`TRY에서 느낀 어려운 점이나 배운 점을 기록한다.

### [느낀 점] : 매서드의 흐름이나 필요성을 확실히 인지하게 된거같고 필요한 모든 메서드 명을 정확히는 암기하지 읺았지만 어떤 기능이 필요한지는 알 수 있게 되었다. 
<br>또한 처음에 실행이 안되는 버그가 있었는데, 아마도 yaml에서 값을 가져오는 @Value의 옵션을 잘 못 넣은 거 같다는 추론을 했는데 정확했다.
<br>

```angular2html
[LocalFileStorage]

@Override
public void deleteFile(String storedName) {

if(storedName == null || storedName.isEmpty()){
return;
}

Path targetPath = uploadPath.resolve(storedName)`.getFileName()`.normalize();

if(!targetPath.startsWith(uploadPath)){
log.warn("삭제 요청 거부. 업로드 디렉터리 외부 경로:" + targetPath);
return;
}
```
- [오류1]:uploadPath.resolve(storedName) -> uploadPath: 업로드 폴더 경로 + .resolve(storedName):파일명을 Path로 변형 + / 추가해서 경로+추가할 파일명을 완성
<br>근데 그 완성된 걍로에 `.getFileName()`를 하면 다시 uploadPath부분을 제외한 storedName 즉 파일 명만 반환됨 -> `.getFileName()`을 지우워야함: uploadPath.resolve(storedName).normalize();

```angular2html
오류 코드 예시.
```
- [오류2]:



<br><br><br>

> ## [긍정 평가]
> - 평타는 침.
---



