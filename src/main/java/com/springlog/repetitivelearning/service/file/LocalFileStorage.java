package com.springlog.repetitivelearning.service.file;

import com.springlog.repetitivelearning.exception.FileStorageException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class LocalFileStorage implements FileStorage{

 private final static Set<String> ALLOWED_EXTENSIONS = Set.of(
      ".jpg", ".jpeg", ".png", ".gif", ".webp", ".bmp", ".svg",
      ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
      ".txt", ".md", ".csv", ".json"
  );

  private final Path uploadPath;

  public LocalFileStorage(
      @Value("${sprintlog.file-directory}") String uploadPathByYaml ){
    this.uploadPath = Paths.get(uploadPathByYaml).toAbsolutePath().normalize();

    try {
      Files.createDirectories(uploadPath);
      log.info("업로드 디렉터리 준비 완료. 경로: {}", uploadPath);
    }catch (IOException e){
      throw new FileStorageException("업로드 디렉터리 생성 실패. 실패 경로: " +  uploadPathByYaml, e);
    }
  }



  @Override
  public String saveFile(MultipartFile file) {
    if(file == null || file.isEmpty()){
      throw new IllegalArgumentException("파일이 비어있습니다.");
    }

    String originalFileName = StringUtils.cleanPath(
        file.getOriginalFilename() == null ? "" : file.getOriginalFilename()
    );

    int lastDotIndex = originalFileName.lastIndexOf('.');
    String extension = (lastDotIndex >= 0) ?
        originalFileName.substring(lastDotIndex).toLowerCase() : "";

    if(!ALLOWED_EXTENSIONS.contains(extension)){
      throw new IllegalArgumentException("허용되지 않은 확장자. 확장자: " + extension);
    }

    String storedFileName = UUID.randomUUID().toString().replace("-", "") + extension;

    Path targetPath = uploadPath.resolve(storedFileName).normalize();

    if(!targetPath.startsWith(uploadPath)){
      throw new FileStorageException(
          "저장 경로가 업로드 디렉토리 외부입니다. 경로:" +targetPath
      );
    }

    try {
      file.transferTo(targetPath);
      log.info("파일 저장완료 저장된 파일명: {}, [원본 파일명: {}, 크기: {}-bytes]",
                               storedFileName,
                                originalFileName,
                                  file.getSize());
      return storedFileName;
    }catch (IOException e){
      throw new FileStorageException(
          "파일 저장 실패. 저장 실패 파일:" +originalFileName,e
      );
    }
  }

  @Override
  public String getFileUrl(String storedName) {
    return "/api/files/" + storedName;
  }

  @Override
  public String getDownloadUrl(String storedName) {
    return "/api/files/download/" + storedName;
  }

  @Override
  public void deleteFile(String storedName) {

    if(storedName == null || storedName.isEmpty()){
      return;
    }

    Path targetPath = uploadPath.resolve(storedName).normalize();

    if(!targetPath.startsWith(uploadPath)){
      log.warn("삭제 요청 거부. 업로드 디렉터리 외부 경로:" + targetPath);
      return;
    }

    try {
      if(Files.deleteIfExists(targetPath)){
        log.info("파일 삭제 완료: " +  storedName);
      }
    }catch (IOException e) {
      log.error("파일 삭제 실패: {}", storedName, e);
    }
  }
}
