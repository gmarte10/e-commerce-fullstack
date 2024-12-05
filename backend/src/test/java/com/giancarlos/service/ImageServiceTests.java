package com.giancarlos.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = "upload.dir=${java.io.tmpdir}/uploads")
public class ImageServiceTests {

    @InjectMocks
    private ImageService imageService;

    private Path tempDir;

    @BeforeEach
    public void setUp() throws IOException {
        tempDir = Files.createTempDirectory("uploads");
        System.setProperty("upload.dir", tempDir.toString());
    }

    @Test
    public void ImageService_GetImageBase64_String_Success() throws IOException {
        String fileName = "test-img.jpg";
        Path file = tempDir.resolve(fileName);
        Files.write(file, "image data".getBytes());

        String base64Img = imageService.getImageBase64(fileName);

        Assertions.assertThat(base64Img).isNotNull();
        Assertions.assertThat(base64Img).startsWith("data:");
    }
}
