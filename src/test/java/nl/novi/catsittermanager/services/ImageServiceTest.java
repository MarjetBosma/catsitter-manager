package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.*;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CatsitterRepository;
import nl.novi.catsittermanager.repositories.FileUploadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private FileUploadRepository fileUploadRepository;

    @Mock
    private CatRepository catRepository;

    @Mock
    private CatsitterRepository catsitterRepository;

    @Mock
    private MultipartFile file;

    private ImageService imageService;

    private final String fileStorageLocation = "uploads";
    private final Path fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();

    @BeforeEach
    void setUp() throws Exception {
        imageService = new ImageService("test/upload/location", fileUploadRepository, catRepository, catsitterRepository);
        Files.createDirectories(fileStoragePath);
    }

    @Test
    void testUploadCatImage_validImageForExistingCatsitterShouldBeUploaded() throws Exception {

        // Given
        UUID catId = UUID.randomUUID();
        Cat cat = CatFactory.randomCat().build();
        String filename = "testfile.jpg";
        String url = "http://localhost/cat/" + catId.toString() + "/upload/" + filename;
        byte[] content = "Test Content".getBytes();

        when(catRepository.findById(catId)).thenReturn(Optional.of(cat));
        when(file.getOriginalFilename()).thenReturn(filename);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(content));
        when(fileUploadRepository.save(any(ImageUpload.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ServletUriComponentsBuilder mockBuilder = Mockito.mock(ServletUriComponentsBuilder.class);
        Mockito.mockStatic(ServletUriComponentsBuilder.class);
        when(ServletUriComponentsBuilder.fromCurrentContextPath()).thenReturn(mockBuilder);
        when(mockBuilder.path(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.toUriString()).thenReturn(url);

        // When
        ImageUpload result = imageService.uploadCatImage(catId, file);

        // Then
        assertNotNull(result);
        assertEquals(filename, result.getFilename());
        assertEquals(file.getContentType(), result.getContentType());
        assertEquals(url, result.getUrl());
        assertEquals(cat, result.getCat());

        verify(catRepository, times(2)).findById(catId);
        verify(fileUploadRepository, times(2)).save(any(ImageUpload.class));
        verify(catRepository, times(2)).save(cat);
    }

    @Test
    void testUploadCatImage_catNotFound() {
        // Given
        UUID catId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

        when(catRepository.findById(catId)).thenReturn(Optional.empty());

        // When & Then
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            imageService.uploadCatImage(catId, file);
        });

        assertEquals("Cat not found with id: " + catId, exception.getMessage());

        verify(catRepository, times(1)).findById(catId);
        verify(fileUploadRepository, never()).save(any(ImageUpload.class));
        verify(catRepository, never()).save(any(Cat.class));
    }

    @Test
    void testUploadCatImage_nullFilename() {
        // Given
        UUID catId = UUID.randomUUID();
        Cat cat = CatFactory.randomCat().build();
        MockMultipartFile file = new MockMultipartFile("file", null, "image/jpeg", "test image content".getBytes());

        String expectedUrl = "http://localhost/cat";
        ServletUriComponentsBuilder builderMock = mock(ServletUriComponentsBuilder.class);
        when(ServletUriComponentsBuilder.fromCurrentContextPath()).thenReturn((ServletUriComponentsBuilder) UriComponentsBuilder.fromHttpUrl(expectedUrl));
        ReflectionTestUtils.setField(ServletUriComponentsBuilder.class, "INSTANCE", builderMock);

        when(catRepository.findById(catId)).thenReturn(Optional.of(cat));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            imageService.uploadCatImage(catId, file);
        });

        assertEquals("File must have an original filename", exception.getMessage());

        verify(catRepository, never()).findById(catId);
        verify(fileUploadRepository, never()).save(any(ImageUpload.class));
        verify(catRepository, never()).save(any(Cat.class));
    }

    @Test
    void uploadCatsitterImage_validImageForExistingCatsitterShouldBeUploaded() throws Exception {
        // Given
        String catsitterId = "catsitter";
        Catsitter catsitter = CatsitterFactory.randomCatsitter().build();
        String filename = "testfile.jpg";
        String url = "http://localhost/catsitter/" + catsitterId + "/upload/" + filename;
        byte[] content = "Test Content".getBytes();

        when(catsitterRepository.findById(catsitterId)).thenReturn(Optional.of(catsitter));
        when(file.getOriginalFilename()).thenReturn(filename);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(content));
        when(fileUploadRepository.save(any(ImageUpload.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ServletUriComponentsBuilder mockBuilder = Mockito.mock(ServletUriComponentsBuilder.class);
        Mockito.mockStatic(ServletUriComponentsBuilder.class);
        when(ServletUriComponentsBuilder.fromCurrentContextPath()).thenReturn(mockBuilder);
        when(mockBuilder.path(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.toUriString()).thenReturn(url);

        // When
        ImageUpload result = imageService.uploadCatsitterImage(catsitterId, file);

        // Then
        assertNotNull(result);
        assertEquals(filename, result.getFilename());
        assertEquals(file.getContentType(), result.getContentType());
        assertEquals(url, result.getUrl());
        assertEquals(catsitter, result.getCatsitter());

        verify(catsitterRepository, times(2)).findById(catsitterId);
        verify(fileUploadRepository, times(2)).save(any(ImageUpload.class));
        verify(catsitterRepository, times(2)).save(catsitter);
    }

    @Test
    void testUploadCatsitterImage_catsitterNotFound() {
        // Given
        String catsitterId = "catsitter";
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

        when(catsitterRepository.findById(catsitterId)).thenReturn(Optional.empty());

        // When & Then
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            imageService.uploadCatsitterImage(catsitterId, file);
        });

        assertEquals("Catsitter not found with id: " + catsitterId, exception.getMessage());

        verify(catsitterRepository, times(1)).findById(catsitterId);
        verify(fileUploadRepository, never()).save(any(ImageUpload.class));
        verify(catsitterRepository, never()).save(any(Catsitter.class));
    }

    @Test
    void testUploadCatsitterImage_nullFilename() {}


    @Test
    void storeFile_fileShouldBeStoredCorrectly() {
    }

    @Test
    void storeFile_issueInStoringFile() {
    }

    @Test
    void testAssignImageToCat_imageShouldBeAssignedToExistingCat() {
    }

    @Test
    void testAssignImageToCat_CatNotFound() {
    }

    @Test
    void testAssignImageToCatsitter_imageShouldBeAssignedToExistingCatsitter() {
    }

    @Test
    void testAssignImageToCatsitter_catsitterNotFound() {
    }

    @Test
    void downloadImage_imageShouldBeDownloadedCorrectly() {
    }

    @Test
    void downloadImage_issueInReadingFile() {
    }

    @Test
    void downloadImage_resourceDoesNotExist() {
    }

    @Test
    void downloadImage_resourceIsNotReadable() {
    }
}