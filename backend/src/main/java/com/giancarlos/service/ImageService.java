package com.giancarlos.service;

import com.giancarlos.exception.ImageNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.UUID;

@Service
@Slf4j
public class ImageService {
    @Value("${upload.dir}")
    private String getUploadDir;

    @Value("${upload.dir:uploads}")
    private String uploadDir;

    public String getImageBase64(String imageURL) {
        if (imageURL == null || imageURL.isEmpty()) {
            throw new ImageNotFoundException("Image url path was not found");
        }
        try {
            Path uploadPath = Paths.get(getUploadDir, imageURL);
            System.out.println(uploadPath);
            // Verify the file exists and is within the uploads directory (security check)
            if (!Files.exists(uploadPath) || !uploadPath.normalize().startsWith(uploadPath.normalize())) {
                throw new ImageNotFoundException("Image file not found or invalid path");
            }

            byte[] imageBytes = Files.readAllBytes(uploadPath);
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            String mimeType = Files.probeContentType(uploadPath);
            return "data:" + mimeType + ";base64," + base64Image;

        } catch (IOException e) {
            log.error("Error reading image file: {}", imageURL, e);
            throw new ImageNotFoundException("Reading image file went wrong");
        }
    }

    public String uploadProductToMemory(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new ImageNotFoundException("No image file provided");
        }
        try {
            Path uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String originalFileName = imageFile.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            Path imagePath = uploadPath.resolve(uniqueFileName);

            // Save the file
            Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            return uniqueFileName;

        } catch (IOException e) {
            System.out.println("Error uploading image\n\n" + e);
            throw new ImageNotFoundException("Uploading image went wrong");
        }
    }
}
