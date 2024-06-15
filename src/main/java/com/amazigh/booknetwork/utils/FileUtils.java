package com.amazigh.booknetwork.utils;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
public class FileUtils {

  public static byte[] readFileFromLocation(String filePath) {
    if (StringUtils.isBlank(filePath)) return null;

    try {
      Path path = new File(filePath).toPath();
      return Files.readAllBytes(path);
    } catch (IOException e) {
      log.warn("No file found in this path {}", filePath);
    }

    return null;
  }
}
