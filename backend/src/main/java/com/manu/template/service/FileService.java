package com.manu.template.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {
    public String storeImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File not found");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File type not supported");
        }
        long maxSize = 200000;
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("File too large");
        }
        String uploadDir = System.getProperty("event.upload.dir", "uploads/");
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.contains("..") || originalFileName.contains("/") || originalFileName.contains("\\")) {
            throw new IllegalArgumentException("File name invalid");
        }
        String fileName = UUID.randomUUID() + "_" + originalFileName;
        Path filePath = Paths.get(uploadDir, fileName);
        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + fileName;
    }
}
