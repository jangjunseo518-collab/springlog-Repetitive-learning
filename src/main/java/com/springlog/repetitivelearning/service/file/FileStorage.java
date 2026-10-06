package com.springlog.repetitivelearning.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {

  // 파일을 저장하고, 저장된 파일명을 돌려준다. (엔티티에 보관해 두었다가 나중에 찾는 데 사용)
  String saveFile(MultipartFile file);

  // 저장된 파일을 브라우저가 "보는" 데 쓰는 URL
  String getFileUrl(String storedName);

  // 저장된 파일을 브라우저가 내 PC에 "다운로드(저장)"하게 하는 URL
  String getDownloadUrl(String storedName);

  // 저장된 파일 삭제
  void deleteFile(String storedName);

}
