package com.amazigh.booknetwork.services.implementation;

import com.amazigh.booknetwork.services.FileStorageService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

  @Value("${application.file.upload.picture-output-path}")
  private String fileUploadPath;

  @Override
  public String saveFile(@Nonnull MultipartFile sourceFile, @Nonnull Integer userId) {
    final String fileUploadSubPath = "users" + separator + userId;

    return uploadFile(sourceFile, fileUploadSubPath);
  }

  private String uploadFile(
      @Nonnull MultipartFile sourceFile,
      @Nonnull String fileUploadSubPath
  ) {
    final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath;
    File targetFolder = new File(finalUploadPath);

    if (!targetFolder.exists()) {
      boolean folderCreated = targetFolder.mkdirs();
      if (!folderCreated) {
        log.warn("Failed to create the target folder: " + targetFolder);
        return null;
      }
    }
    final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
    String targetFilePath = finalUploadPath + separator + currentTimeMillis() + "." + fileExtension;
    Path targetPath = Paths.get(targetFilePath);
    try {
      Files.write(targetPath, sourceFile.getBytes());
      log.info("File saved to: " + targetFilePath);
      return targetFilePath;
    } catch (IOException e) {
      log.error("File was not saved", e);
    }
    return null;
  }

  private String getFileExtension(String originalFilename) {
    if (originalFilename == null || originalFilename.isEmpty()) return null;

    // file.jpg
    int lastDotIndex = originalFilename.lastIndexOf(".");
    if (lastDotIndex == -1) return null;

    // .JPG -> .jpg
    return originalFilename.substring(lastDotIndex+1).toLowerCase();
  }
}
