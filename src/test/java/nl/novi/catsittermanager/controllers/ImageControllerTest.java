package nl.novi.catsittermanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.catsittermanager.config.SecurityConfig;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.exceptions.FileNotFoundException;
import nl.novi.catsittermanager.exceptions.InvalidTypeException;
import nl.novi.catsittermanager.filters.JwtAuthorizationFilter;
import nl.novi.catsittermanager.models.ImageUpload;
import nl.novi.catsittermanager.services.ImageService;
import nl.novi.catsittermanager.utils.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {ImageController.class, ExceptionController.class})
@Import({JwtUtil.class, JwtAuthorizationFilter.class, SecurityConfig.class, TestConfig.class})
@ActiveProfiles("test")
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenUploadCatImage_thenImageShouldBeUploaded() throws Exception {

        // Arrange
        UUID catId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "catimage.jpg", "image/jpeg", "test image content".getBytes());
        ImageUpload imageUpload = new ImageUpload("catimage.jpg", "image/jpeg", "/catimage.jpg");

        when(imageService.uploadCatImage(any(UUID.class), any(MockMultipartFile.class))).thenReturn(imageUpload);

        // Act & Assert
        mockMvc.perform(multipart("/api/cat/" + catId + "/images")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Image uploaded"));

        verify(imageService).uploadCatImage(any(UUID.class), any(MockMultipartFile.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenUploadCatsitterImage_thenImageShouldBeUploaded() throws Exception {

        // Arrange
        String username = "testuser";
        MockMultipartFile file = new MockMultipartFile("file", "catsitterimage.jpg", "image/jpeg", "test image content".getBytes());
        ImageUpload imageUpload = new ImageUpload("catsitterimage.jpg", "image/jpeg", "src/test/upload/catsitterimage.jpg");

        when(imageService.uploadCatsitterImage(any(String.class), any(MockMultipartFile.class))).thenReturn(imageUpload);

        // Act & Assert
        mockMvc.perform(multipart("/api/catsitter/" + username + "/images")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Image uploaded"));

        verify(imageService).uploadCatsitterImage(any(String.class), any(MockMultipartFile.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidType_whenUploadImage_thenIllegalArgumentException() throws Exception {

        // Arrange
        String invalidType = "invalidtype";
        MockMultipartFile file = new MockMultipartFile("file", "catimage.jpg", "image/jpeg", "test image content".getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/api/invalidtype/testuser/images")
                        .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertInstanceOf(InvalidTypeException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Invalid type: " + invalidType, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenMissingFile_whenUploadImage_thenBadRequest() throws Exception {

        // Arrange
        String type = "catsitter";
        String username = "testcatsitter";
        // No file attached

        // Act & Assert
        mockMvc.perform(multipart("/api/" + type + "/" + username + "/images"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDownloadImage_catImage_succesfulDownload() throws Exception {

        // Arrange
        UUID catId = UUID.fromString(UUID.randomUUID().toString());
        String filename = "catimage.jpg";
        UrlResource resource = new UrlResource(Paths.get("src/test/resources/images/downloads/catimage.jpg").toUri());

        when(imageService.downloadImage(filename)).thenReturn(resource);

        // Act & Assert
        mockMvc.perform(get("/api/cat/" + catId + "/images/" + filename))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + resource.getFilename()));

        verify(imageService).downloadImage(filename);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDownloadImage_catsitterImage_succesfulDownload() throws Exception {

        // Arrange
        String username = "testuser";
        String filename = "catsitterimage.jpg";
        UrlResource resource = new UrlResource(Paths.get("src/test/resources/images/downloads/catsitterimage.jpg").toUri());

        when(imageService.downloadImage(filename)).thenReturn(resource);

        // Act & Assert
        mockMvc.perform(get("/api/catsitter/" + username + "/images/" + filename))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + resource.getFilename()));

        verify(imageService).downloadImage(filename);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenNonExistentFile_whenDownloadImage_thenNotFoundShouldBeReturned() throws Exception {

        // Arrange
        String type = "catsitter";
        String username = "testuser";
        String filename = "nonexistentimage.jpg";

        when(imageService.downloadImage(filename)).thenThrow(new FileNotFoundException("File does not exist or is not readable"));

        // Act & Assert
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/{type}/{id}/images/{filename}", type, username, filename))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("File does not exist or is not readable"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        verify(imageService, Mockito.times(1)).downloadImage(filename);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenUnsupportedMimeType_whenDownloadImage_thenDefaultMimeTypeShouldBeReturned() throws Exception {

        // Arrange
        String type = "cat";
        String id = UUID.randomUUID().toString();
        String filename = "unsupportedfile";

        Resource resource = new ByteArrayResource("test content".getBytes()) {
            @Override
            public File getFile() throws IOException {
                throw new IOException("No file available");
            }
        };

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", filename, "application/octet-stream", "test content".getBytes());

        when(imageService.downloadImage(anyString())).thenReturn(mockMultipartFile.getResource());

        // Act & Assert
        mockMvc.perform(get("/api/" + type + "/" + id + "/images/" + filename))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + filename))
                .andExpect(content().string("test content"));
    }
}
