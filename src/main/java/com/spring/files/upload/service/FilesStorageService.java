package com.spring.files.upload.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
  public void init();

  public void save(MultipartFile file);

  public void deleteAll();

  public String getRecordByKey(String key) throws IOException;

  public List<String[]> deleteRecordByKey(String key) throws IOException;
}
