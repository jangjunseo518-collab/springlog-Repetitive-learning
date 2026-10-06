package com.springlog.repetitivelearning.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {

  String saveFile(MultipartFile file);

  String getFileUrl(String storedName);
  String getDownloadUrl(String storedName);

  void deleteFile(String storedName);

}
