package nl.novi.catsittermanager.controllers;

import nl.novi.catsittermanager.config.SecurityConfig;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.filters.JwtAuthorizationFilter;
import nl.novi.catsittermanager.models.ImageUpload;
import nl.novi.catsittermanager.services.ImageService;
import nl.novi.catsittermanager.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Paths;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    @ExtendWith(SpringExtension.class)
    @WebMvcTest(ImageController.class)
    @Import({JwtUtil.class, JwtAuthorizationFilter.class, SecurityConfig.class, TestConfig.class})
    @ActiveProfiles("test")
    class ImageControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ImageService imageService;

        @BeforeEach
        void setUp() {
            mockMvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) mockMvc.getDispatcherServlet().getWebApplicationContext()).build();
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void testUploadCatImage() throws Exception {
            // Given
            UUID catId = UUID.randomUUID();
            MockMultipartFile file = new MockMultipartFile("file", "testfile.jpg", "image/jpeg", "test image content".getBytes());
            ImageUpload imageUpload = new ImageUpload("testfile.jpg", "image/jpeg", "/testfile.jpg");

            when(imageService.uploadCatImage(any(UUID.class), any(MockMultipartFile.class))).thenReturn(imageUpload);

            // When & Then
            mockMvc.perform(multipart("/api/cat/" + catId + "/images")
                            .file(file))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Image uploaded"));

            Mockito.verify(imageService).uploadCatImage(any(UUID.class), any(MockMultipartFile.class));
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void testUploadCatsitterImage() throws Exception {
            // Given
            String username = "testuser";
            MockMultipartFile file = new MockMultipartFile("file", "testfile.jpg", "image/jpeg", "test image content".getBytes());
            ImageUpload imageUpload = new ImageUpload("testfile.jpg", "image/jpeg", "/upload/testfile.jpg");

            when(imageService.uploadCatsitterImage(any(String.class), any(MockMultipartFile.class))).thenReturn(imageUpload);

            // When & Then
            mockMvc.perform(multipart("/api/catsitter/" + username + "/images")
                            .file(file))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Image uploaded"));

            Mockito.verify(imageService).uploadCatsitterImage(any(String.class), any(MockMultipartFile.class));
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void testDownloadImage_catImage() throws Exception {
            // Given
            UUID catId = UUID.fromString(UUID.randomUUID().toString());
            String filename = "testfile.jpg";
            UrlResource resource = new UrlResource(Paths.get("src/test/resources/images/downloads/testfile.jpg").toUri());

            when(imageService.downloadImage(filename)).thenReturn(resource);

            // When & Then
            mockMvc.perform(get("/api/cat/" + catId + "/images/" + filename))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                    .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + resource.getFilename()));

            Mockito.verify(imageService).downloadImage(filename);
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void testDownloadImage_catsitterImage() throws Exception {
            // Given
            String username = "testuser";
            String filename = "testfile.jpg";
            UrlResource resource = new UrlResource(Paths.get("src/test/resources/images/downloads/testfile.jpg").toUri());

            when(imageService.downloadImage(filename)).thenReturn(resource);

            // When & Then
            mockMvc.perform(get("/api/catsitter/" + username + "/images/" + filename))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                    .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + resource.getFilename()));

            Mockito.verify(imageService).downloadImage(filename);
        }
    }
