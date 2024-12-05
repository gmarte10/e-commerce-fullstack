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

    @Value("${upload.dir:uploads}")
    private String uploadDir;

    public String getImageBase64(String imgURL) {
        validateInput(imgURL, "Image URL path was not provided");

        try {
            Path imgPath = Paths.get(uploadDir, imgURL).normalize();
            verifyFilePath(imgPath);
            byte[] imgBytes = Files.readAllBytes(imgPath);
            String base64Img = Base64.getEncoder().encodeToString(imgBytes);
            String mimeType = Files.probeContentType(imgPath);

            return String.format("data:%s;base64,%s", mimeType, base64Img);
        } catch (IOException e) {
            log.error("Error reading image file: {}", imgURL, e);
            throw new ImageNotFoundException("Failed to read image file");
        }
    }

    private void verifyFilePath(Path filePath) {
        if (!Files.exists(filePath) || !filePath.startsWith(Paths.get(uploadDir))) {
            throw new ImageNotFoundException("Image file was not found or invalid path");
        }
    }

    private void validateInput(Object input, String eMsg) {
        boolean isNull = input == null;
        boolean isEmptyString = input instanceof String && ((String) input).isEmpty();
        boolean isEmptyMultipartFile = input instanceof MultipartFile && ((MultipartFile) input).isEmpty();
        if (isNull || isEmptyString || isEmptyMultipartFile) {
            throw new ImageNotFoundException(eMsg);
        }
    }

    public String uploadProductToMemory(MultipartFile imgFile) {
        validateInput(imgFile, "No image MultipartFile provided");

        try {
            Path uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir);
            createDirectoryIfNotExist(uploadPath);

            String uniqueName = generateUniqueFileName(imgFile.getOriginalFilename());
            Path fPath = uploadPath.resolve(uniqueName);

            // Save the file
            Files.copy(imgFile.getInputStream(), fPath, StandardCopyOption.REPLACE_EXISTING);
            return uniqueName;

        } catch (IOException e) {
            log.error("Error uploading the image MultipartFile", e);
            throw new ImageNotFoundException("Failed to upload image");
        }
    }

    private void createDirectoryIfNotExist(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    private String generateUniqueFileName(String origName) {
        String fExtension = (origName != null && origName.contains("."))
                ? origName.substring(origName.lastIndexOf("."))
                : "";
        return UUID.randomUUID() + fExtension;
    }
}
