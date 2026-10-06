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

@Service
@Slf4j
public class LocalFileStorage implements FileStorage{

  private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
      ".jpg", ".jpeg", ".png", ".gif", ".webp", ".bmp", ".svg",
      ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
      ".txt", ".md", ".csv", ".json"
  );

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

  @Override
  public String saveFile(MultipartFile file) {
    if(file == null || file.isEmpty()) {
      throw new IllegalArgumentException("파일이 없습니다.");
    }

    String originalFilename = StringUtils.cleanPath(
        file.getOriginalFilename() == null ?
            "unKnown" : file.getOriginalFilename()
    );

    int lastDotIndex = originalFilename.lastIndexOf('.');

    String extension = (lastDotIndex >= 0) ?
                       originalFilename.substring(lastDotIndex)
                       .toLowerCase() : "";

    if(!ALLOWED_EXTENSIONS.contains(extension)) {
      throw new IllegalArgumentException(
          "허용하지 않는 확장자 입니다. 업로드 확장자:" + extension
      );
    }

    String savedFileName = UUID.randomUUID()
        .toString().replace("-", "")+extension;

    Path targetPath = uploadPath.resolve(savedFileName).normalize();

    if(!targetPath.startsWith(uploadPath)) {
      throw new FileStorageException(
          "저장 경로가 업데이트 디렉터리 외부입니다. 경로:" + targetPath);
    }

    try{
      file.transferTo(targetPath);
      log.info("파일 저장 완료. 저장된 파일 이름: {} [원본 파일 이름: {}, 크기: {}]",
          savedFileName, originalFilename, file.getSize());
      return savedFileName;
    }catch (IOException e) {
      throw new FileStorageException(
          "파일 저장 실패" + originalFilename, e
      );
    }

  }

  @Override
  public String getFileUrl(String storedFileName) {
    return "/api/files/" + storedFileName;
  }

  @Override
  public String getDownloadUrl(String storedFileName) {
    return "/api/files/" + storedFileName + "?download=1";
  }

  @Override
  public void deleteFile(String storedFileName) {
    if(storedFileName == null || storedFileName.isEmpty()) {
      return;
    }
    Path target = uploadPath.resolve(storedFileName).normalize();
    if(!target.startsWith(uploadPath)) {
      log.warn("삭제 요청 거부 - 업로드 디렉터리 외부 경로: {}", storedFileName);
      return;
    }
    try {
      if(Files.deleteIfExists(target)) {
        log.info("파일 삭제: {}", target);
      }
    }catch (IOException e) {
      log.error("파일 삭제 실패: {}",  storedFileName, e);
    }

  }
}
