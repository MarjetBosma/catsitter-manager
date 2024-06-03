package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.*;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CatsitterRepository;
import nl.novi.catsittermanager.repositories.FileUploadRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryStream;
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

    private Path fileStoragePath;

    @BeforeEach
    void setUp() throws Exception {
        String fileStorageLocation = "src/test/resources/uploads";
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        imageService = new ImageService(fileStorageLocation, fileUploadRepository, catRepository, catsitterRepository);
        Files.createDirectories(fileStoragePath);
    }

    @AfterEach
    void cleanUp() throws IOException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(fileStoragePath)) {
            for (Path path : directoryStream) {
                Files.delete(path);
            }
        }
    }

    // todo: Deze test slaagt als je hem los draait, maar faalt als je de hele klasse draait...
    @Test
    void testUploadCatImage_imageShouldBeUploadedAssignedAndSaved() throws Exception {
        // Given
        UUID catId = UUID.randomUUID();
        Cat cat = CatFactory.randomCat().build();
        String filename = "testfile.jpg";
        String url = "http://localhost:8080/api/cat/" + catId + "/images/" + filename;
        byte[] content = "Test Content".getBytes();

        when(catRepository.findById(catId)).thenReturn(Optional.of(cat));
        when(file.getOriginalFilename()).thenReturn(filename);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(content));
        when(fileUploadRepository.save(any(ImageUpload.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ServletRequestAttributes requestAttributes = Mockito.mock(ServletRequestAttributes.class);
        RequestContextHolder.setRequestAttributes(requestAttributes);
        ServletUriComponentsBuilder mockBuilder = Mockito.mock(ServletUriComponentsBuilder.class);
        mockStatic(ServletUriComponentsBuilder.class);

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
        assertEquals(result, cat.getImage());

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
        MockMultipartFile file = new MockMultipartFile("file", null, "image/jpeg", "test image content".getBytes());

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
    void uploadCatsitter_imageShouldBeUploadedAssignedAndSaved() throws Exception {
        // Given
        String catsitterId = "catsitter";
        Catsitter catsitter = CatsitterFactory.randomCatsitter().build();
        String filename = "testfile1.jpg";
        String url = "http://localhost:8080/api/catsitter/" + catsitterId + "/uploads/" + filename;
        byte[] content = "Test Content".getBytes();

        when(catsitterRepository.findById(catsitterId)).thenReturn(Optional.of(catsitter));
        when(file.getOriginalFilename()).thenReturn(filename);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(content));
        when(fileUploadRepository.save(any(ImageUpload.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ServletRequestAttributes requestAttributes = Mockito.mock(ServletRequestAttributes.class);
        RequestContextHolder.setRequestAttributes(requestAttributes);
        ServletUriComponentsBuilder mockBuilder = Mockito.mock(ServletUriComponentsBuilder.class);
        mockStatic(ServletUriComponentsBuilder.class);

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
        assertEquals(result, catsitter.getImage());

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
    void testUploadCatsitterImage_nullFilename() {
        // Given
        String catsitterId = "catsitter";
        MockMultipartFile file = new MockMultipartFile("file", null, "image/jpeg", "test image content".getBytes());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            imageService.uploadCatsitterImage(catsitterId, file);
        });

        assertEquals("File must have an original filename", exception.getMessage());

        verify(catsitterRepository, never()).findById(catsitterId);
        verify(fileUploadRepository, never()).save(any(ImageUpload.class));
        verify(catsitterRepository, never()).save(any(Catsitter.class));
    }


    @Test
    void storeFile_fileShouldBeStoredCorrectly() throws IOException {
        // Given
        UUID catId = UUID.randomUUID();
        String filename = "testfile.jpg";
        String url = "http://localhost/cat/" + catId + "/" + filename;

        byte[] content = "Test content".getBytes();
        String contentType = "image/jpeg";
        MultipartFile file = new MockMultipartFile("file", filename, contentType, content);

        when(fileUploadRepository.save(any(ImageUpload.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        String storedFilename = imageService.storeFile(file, url);

        // Then
        assertEquals(filename, storedFilename);
        Path expectedPath = fileStoragePath.resolve(filename);
        assertTrue(Files.exists(expectedPath));

        byte[] savedContent = Files.readAllBytes(expectedPath);
        assertArrayEquals(content, savedContent);

        verify(fileUploadRepository, times(1)).save(any(ImageUpload.class));
    }

    @Test
    void storeFile_issueInStoringFile() {
        // Given
        UUID catId = UUID.randomUUID();
        String filename = "testfile.jpg";
        String url = "http://localhost/cat/" + catId + "images/uploads/" + filename;
        byte[] content = "Test Content".getBytes();
        String contentType = "image/jpeg";

        MultipartFile file = new MockMultipartFile("file", filename, contentType, content);
        MultipartFile fileWithIOException = mock(MultipartFile.class);
        when(fileWithIOException.getOriginalFilename()).thenReturn(filename);
        try {
            when(fileWithIOException.getInputStream()).thenThrow(new IOException("Test IOException"));
        } catch (IOException e) {
            // This catch block will never be executed because the exception is mocked
        }

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imageService.storeFile(fileWithIOException, url);
        });

        assertEquals("Issue in storing the file", exception.getMessage());

        verify(fileUploadRepository, never()).save(any(ImageUpload.class));
    }

    @Test
    void testAssignImageToCat_imageShouldBeAssignedToExistingCat() {
        // Given
        UUID catId = UUID.randomUUID();
        Cat cat = CatFactory.randomCat().build();
        ImageUpload imageUpload = new ImageUpload("testfile.jpg", "image/jpeg", "http://localhost/cat/" + catId + "/uploads" +
                "/testfile.jpg");
        when(catRepository.findById(catId)).thenReturn(Optional.of(cat));

        // When
        imageService.assignImageToCat(imageUpload, catId);

        // Then
        assertEquals(imageUpload, cat.getImage());
        verify(catRepository, times(1)).findById(catId);
        verify(catRepository, times(1)).save(cat);
    }

    @Test
    void testAssignImageToCat_catNotFound() {
        // Given
        UUID catId = UUID.randomUUID();
        ImageUpload imageUpload = new ImageUpload("testfile.jpg", "image/jpeg", "http://localhost/cat/" + catId + "/images/uploads/testfile.jpg");
        when(catRepository.findById(catId)).thenReturn(Optional.empty());

        // When & Then
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            imageService.assignImageToCat(imageUpload, catId);
        });

        assertEquals("No cat found with this id.", exception.getMessage());
        verify(catRepository, times(1)).findById(catId);
        verify(catRepository, never()).save(any(Cat.class));
    }

    @Test
    void testAssignImageToCatsitter_imageShouldBeAssignedToExistingCatsitter() {
        // Given
        String catsitterId = "catsitter";
        Catsitter catsitter = CatsitterFactory.randomCatsitter().build();
        ImageUpload imageUpload = new ImageUpload("testfile1.jpg", "image/jpeg", "http://localhost/catsitter/" + catsitterId + "/images/uploads/testfile1.jpg");
        when(catsitterRepository.findById(catsitterId)).thenReturn(Optional.of(catsitter));

        // When
        imageService.assignImageToCatsitter(imageUpload, catsitterId);

        // Then
        assertEquals(imageUpload, catsitter.getImage());
        verify(catsitterRepository, times(1)).findById(catsitterId);
        verify(catsitterRepository, times(1)).save(catsitter);
    }

    @Test
    void testAssignImageToCatsitter_catsitterNotFound() {
        // Given
        String catsitterId = "catsitter";
        ImageUpload imageUpload = new ImageUpload("testfile1.jpg", "image/jpeg", "http://localhost/catsitter/" + catsitterId + "/images/uploads/testfile1.jpg");
        when(catsitterRepository.findById(catsitterId)).thenReturn(Optional.empty());

        // When & Then
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            imageService.assignImageToCatsitter(imageUpload, catsitterId);
        });

        assertEquals("No catsitter found with this username.", exception.getMessage());
        verify(catsitterRepository, times(1)).findById(catsitterId);
        verify(catsitterRepository, never()).save(any(Catsitter.class));
    }

    @Test
    void downloadImage_imageShouldBeDownloadedCorrectly() throws IOException {
        // Given
        String filename = "testfile.jpg";
        String fileStorageLocation = "src/test/resources/images/downloads/";
        Path filePath = Paths.get(fileStorageLocation).toAbsolutePath().resolve(filename);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, "Test Content".getBytes());

        // When
        Resource resource = imageService.downloadImage(filename);

        // Then
        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
        assertEquals(filePath.toUri(), resource.getURI());

        // Clean up
        Files.deleteIfExists(filePath);
    }

    @Test
    void downloadImage_resourceDoesNotExistOrIsNotReadable() {
        // Given
        String filename = "non_existent_file.jpg";
        String fileStorageLocation = "src/test/resources/images/downloads/";

        imageService = new ImageService(fileStorageLocation, fileUploadRepository, catRepository, catsitterRepository);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imageService.downloadImage(filename);
        });

        assertEquals("File doesn't exist or is not readable", exception.getMessage());
    }

    @Test
    void downloadImage_MalformedURL() throws MalformedURLException {
        // Given
        String filename = "testfile.jpg";
        String fileStorageLocation = "src/test/resources/images/downloads/";

        ImageService imageServiceSpy = Mockito.spy(new ImageService(fileStorageLocation, fileUploadRepository, catRepository, catsitterRepository));
        Mockito.doThrow(new MalformedURLException("Malformed URL")).when(imageServiceSpy).createUrlResource(Mockito.any(Path.class));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imageServiceSpy.downloadImage(filename);
        });

        assertEquals("Issue in reading the file", exception.getMessage());
    }

    @Test
    void getFilePath_shouldReturnCorrectFilePath() {
        // Given
        String filename = "testfile.jpg";
        String fileStorageLocation = "src/test/resources/images/uploads/";

        ImageService imageService = new ImageService(fileStorageLocation, fileUploadRepository, catRepository, catsitterRepository);

        // When
        Path filePath = imageService.getFilePath(filename);

        // Then
        Path expectedPath = Paths.get(fileStorageLocation).toAbsolutePath().resolve(filename);
        assertEquals(expectedPath, filePath);
    }

    @Test
    void createUrlResource_shouldCreateUrlResource() throws Exception {
        // Given
        String fileStoragePath = "src/test/resources/images/uploads";
        Path path = Paths.get(fileStoragePath, "testfile.jpg");
        Files.createDirectories(path.getParent());
        Files.createFile(path);

        // When
        UrlResource urlResource = imageService.createUrlResource(path);

        // Then
        assertNotNull(urlResource);
        assertEquals(path.toUri(), urlResource.getURI());
    }

    @Test
    void createUrlResource_shouldThrowMalformedURLException() throws MalformedURLException {
        // Given
        Path path = Paths.get("src/test/resources/images/uploads/testfile.jpg");
        ImageService imageServiceSpy = Mockito.spy(new ImageService("src/test/resources/uploads/", fileUploadRepository, catRepository, catsitterRepository));
        doThrow(new MalformedURLException("Malformed URL")).when(imageServiceSpy).createUrlResource(Mockito.any(Path.class));

        // When & Then
        assertThrows(MalformedURLException.class, () -> {
            imageServiceSpy.createUrlResource(path);
        });
    }
}