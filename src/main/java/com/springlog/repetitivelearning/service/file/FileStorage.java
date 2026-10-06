package com.springlog.repetitivelearning.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {

  String saveFile(MultipartFile file);
  String getFileUrl(String storedFileName);
  String getDownloadUrl(String storedFileName);
  void deleteFile(String storedFileName);
}
