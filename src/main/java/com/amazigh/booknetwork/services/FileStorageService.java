package com.amazigh.booknetwork.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
  String saveFile(MultipartFile file, Integer userId);
}
