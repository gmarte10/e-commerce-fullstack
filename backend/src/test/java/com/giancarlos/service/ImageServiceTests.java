package com.giancarlos.service;

import com.giancarlos.exception.ImageNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTests {
    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private ImageService imageService;

    @TempDir
    private Path tempDir;

    @Test
    public void ImageService_GetImageBase64_ValidImage_ReturnString() throws IOException {
        // Arrange
        String fileName = "test-image.png";
        String mimeType = "image/png";
        Path imagePath = tempDir.resolve(fileName);
        Files.write(imagePath, "sample image data".getBytes()); // Create a temporary file

        ReflectionTestUtils.setField(imageService, "uploadDir", tempDir.toString());

        // Act
        String result = imageService.getImageBase64(fileName);

        // Assert
        Assertions.assertTrue(result.startsWith("data:" + mimeType + ";base64,"));
    }

    @Test
    public void ImageService_GetImageBase64_NonExistingImage_ThrowImageNotFoundException() throws IOException {
        // Arrange
        String nonExistingImg = "non-existing.png";
        ReflectionTestUtils.setField(imageService, "uploadDir", tempDir.toString());

        assertThrows(ImageNotFoundException.class, () -> imageService.getImageBase64(nonExistingImg));
    }

    @Test
    public void ImageService_UploadProductImageToLocal_ValidImage_ReturnString() throws IOException {
        byte[] fContent = "data".getBytes();
        String origName = "img.png";

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn(origName);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(fContent));

        ReflectionTestUtils.setField(imageService, "uploadDir", tempDir.toString());

        String result = imageService.uploadProductImageToLocal(multipartFile);
        Path uploaded = tempDir.resolve(result);
        Assertions.assertTrue(Files.exists(uploaded));
    }

    @Test
    public void ImageService_UploadProductImageToLocal_NoImage_ThrowImageNotFoundException() {

        when(multipartFile.isEmpty()).thenReturn(true);

        ReflectionTestUtils.setField(imageService, "uploadDir", tempDir.toString());

        assertThrows(ImageNotFoundException.class, () -> imageService.uploadProductImageToLocal(multipartFile));
    }


}
