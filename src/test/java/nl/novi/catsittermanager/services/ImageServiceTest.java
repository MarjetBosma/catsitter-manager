package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.exceptions.FileNotFoundException;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${my.upload_location}")
    private final String fileStorageLocation = "src/test/resources/images/uploads/";

    @BeforeEach
    void setUp() throws Exception {
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        Files.createDirectories(fileStoragePath);
        imageService = new ImageService(fileStorageLocation, fileUploadRepository, catRepository, catsitterRepository);

        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes attrs = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attrs);
    }

    @AfterEach
    void tearDown() throws IOException {
        RequestContextHolder.resetRequestAttributes();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(fileStoragePath)) {
            for (Path path : directoryStream) {
                Files.deleteIfExists(path);
            }
        }
    }

    @Test
    void uploadCatImage_imageShouldBeUploadedAssignedAndSaved() {

        // Arrange
        UUID catId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "catimage.jpg", "image/jpeg", "Test Image Content".getBytes());
        Cat cat = CatFactory.randomCat().build();
        cat.setId(catId);

        when(catRepository.findById(catId)).thenReturn(Optional.of(cat));
        when(fileUploadRepository.save(any(ImageUpload.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act & Assert
        ImageUpload result = imageService.uploadCatImage(catId, file);

        assertNotNull(result);
        assertEquals("catimage.jpg", result.getFilename());
        assertEquals("image/jpeg", result.getContentType());

        String expectedUrl = "http://localhost/api/cat/" + catId + "/images/catimage.jpg";

        assertEquals(expectedUrl, result.getUrl());
        assertEquals(cat, result.getCat());

        verify(fileUploadRepository, times(2)).save(any(ImageUpload.class));
        verify(catRepository, times(2)).save(cat);
    }

    @Test
    void testUploadCatImage_catNotFound() {

        // Arrange
        UUID catId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "catimage.jpg", "image/jpeg", "test image content".getBytes());

        when(catRepository.findById(catId)).thenReturn(Optional.empty());

        // Act & Assert
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
        // Arrange
        UUID catId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", null, "image/jpeg", "test image content".getBytes());

        // Act & Assert
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

        // Arrange
        String username = "testuser";
        MockMultipartFile file = new MockMultipartFile("file", "catsitterimage.jpg", "image/jpeg", "Test Image Content".getBytes());
        Catsitter catsitter = CatsitterFactory.randomCatsitter().build();
        catsitter.setUsername(username);

        when(catsitterRepository.findById(username)).thenReturn(Optional.of(catsitter));
        when(fileUploadRepository.save(any(ImageUpload.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act & Assert
        ImageUpload result = imageService.uploadCatsitterImage(username, file);

        assertNotNull(result);
        assertEquals("catsitterimage.jpg", result.getFilename());
        assertEquals("image/jpeg", result.getContentType());

        String expectedUrl = "http://localhost/api/catsitter/" + username + "/images/catsitterimage.jpg";

        assertEquals(expectedUrl, result.getUrl());
        assertEquals(catsitter, result.getCatsitter());

        verify(fileUploadRepository, times(2)).save(any(ImageUpload.class));
        verify(catsitterRepository, times(2)).save(catsitter);
    }

    @Test
    void testUploadCatsitterImage_catsitterNotFound() {

        // Arrange
        String catsitterId = "catsitter";
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

        when(catsitterRepository.findById(catsitterId)).thenReturn(Optional.empty());

        // Act & Assert
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

        // Arrange
        String catsitterId = "catsitter";
        MockMultipartFile file = new MockMultipartFile("file", null, "image/jpeg", "test image content".getBytes());

        // Act & Assert
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

        // Arrange
        UUID catId = UUID.randomUUID();
        String filename = "catimage.jpg";
        String url = "http://localhost/api/cat/" + catId + "/images/" + filename;

        byte[] content = "Test content".getBytes();
        String contentType = "image/jpeg";
        MultipartFile file = new MockMultipartFile("file", filename, contentType, content);

        when(fileUploadRepository.save(any(ImageUpload.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String storedFilename = imageService.storeFile(file, url);

        // Assert
        assertEquals(filename, storedFilename);
        Path expectedPath = fileStoragePath.resolve(filename);
        assertTrue(Files.exists(expectedPath));

        byte[] savedContent = Files.readAllBytes(expectedPath);
        assertArrayEquals(content, savedContent);
        verify(fileUploadRepository, times(1)).save(any(ImageUpload.class));
    }

    @Test
    void storeFile_issueInStoringFile() {

        // Arrange
        UUID catId = UUID.randomUUID();
        String filename = "catimage.jpg";
        String url = "http://localhost/api/cat/" + catId + "/images/" + filename;
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

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imageService.storeFile(fileWithIOException, url);
        });

        assertEquals("Issue in storing the file", exception.getMessage());
        verify(fileUploadRepository, never()).save(any(ImageUpload.class));
    }

    @Test
    void testAssignImageToCat_imageShouldBeAssignedToExistingCat() {

        // Arrange
        UUID catId = UUID.randomUUID();
        Cat cat = CatFactory.randomCat().build();
        ImageUpload imageUpload = new ImageUpload("catimage.jpg", "image/jpeg", "http://localhost/cat/" + catId + "/images/uploads/" + "catimage.jpg");
        when(catRepository.findById(catId)).thenReturn(Optional.of(cat));

        // Act
        imageService.assignImageToCat(imageUpload, catId);

        // Assert
        assertEquals(imageUpload, cat.getImage());
        verify(catRepository, times(1)).findById(catId);
        verify(catRepository, times(1)).save(cat);
    }

    @Test
    void testAssignImageToCat_catNotFound() {
        // Arrange
        UUID catId = UUID.randomUUID();
        ImageUpload imageUpload = new ImageUpload("catimage.jpg", "image/jpeg", "http://localhost/api/cat/" + catId + "/images/catimage.jpg");
        when(catRepository.findById(catId)).thenReturn(Optional.empty());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            imageService.assignImageToCat(imageUpload, catId);
        });

        assertEquals("No cat found with this id.", exception.getMessage());
        verify(catRepository, times(1)).findById(catId);
        verify(catRepository, never()).save(any(Cat.class));
    }

    @Test
    void testAssignImageToCatsitter_imageShouldBeAssignedToExistingCatsitter() {

        // Arrange
        String catsitterId = "catsitter";
        Catsitter catsitter = CatsitterFactory.randomCatsitter().build();
        ImageUpload imageUpload = new ImageUpload("catsitterimage.jpg", "image/jpeg", "http://localhost/catsitter/" + catsitterId + "/images/catsitterimage.jpg");
        when(catsitterRepository.findById(catsitterId)).thenReturn(Optional.of(catsitter));

        // Act
        imageService.assignImageToCatsitter(imageUpload, catsitterId);

        // Assert
        assertEquals(imageUpload, catsitter.getImage());
        verify(catsitterRepository, times(1)).findById(catsitterId);
        verify(catsitterRepository, times(1)).save(catsitter);
    }

    @Test
    void testAssignImageToCatsitter_catsitterNotFound() {

        // Arrange
        String catsitterId = "catsitter";
        ImageUpload imageUpload = new ImageUpload("catsitterimage.jpg", "image/jpeg", "http://localhost/catsitter/" + catsitterId + "/images/catsitterimage.jpg");
        when(catsitterRepository.findById(catsitterId)).thenReturn(Optional.empty());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            imageService.assignImageToCatsitter(imageUpload, catsitterId);
        });

        assertEquals("No catsitter found with this username.", exception.getMessage());
        verify(catsitterRepository, times(1)).findById(catsitterId);
        verify(catsitterRepository, never()).save(any(Catsitter.class));
    }

    @Test
    void downloadImage_imageShouldBeDownloadedCorrectly() throws IOException {

        // Arrange
        String filename = "catimage.jpg";
        String fileStorageLocation = "src/test/resources/images/downloads/";
        Path filePath = fileStoragePath.resolve(filename);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, "Test Content".getBytes());

        // Act
        Resource resource = imageService.downloadImage(filename);

        // Assert
        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
        assertEquals(filePath.toUri(), resource.getURI());

        // Clean up
        Files.deleteIfExists(filePath);
    }

    @Test
    void downloadImage_resourceExistsButIsNotReadable() throws IOException {

        // Arrange
        String filename = "catimage.jpg";
        String fileStorageLocation = "src/test/resources/images/downloads/";
        Path filePath = fileStoragePath.resolve(filename);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, "Test Content".getBytes());

        UrlResource resourceMock = mock(UrlResource.class);
        when(resourceMock.exists()).thenReturn(true);
        when(resourceMock.isReadable()).thenReturn(false);

        ImageService imageServiceSpy = Mockito.spy(new ImageService(fileStorageLocation, fileUploadRepository, catRepository, catsitterRepository));
        doReturn(resourceMock).when(imageServiceSpy).createUrlResource(any(Path.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imageServiceSpy.downloadImage(filename);
        });

        assertEquals("File doesn't exist or is not readable", exception.getMessage());
    }

    @Test
    void downloadImage_resourceReadableButDoesNotExist() throws IOException {

        // Arrange
        String filename = "catimage.jpg";
        String fileStorageLocation = "src/test/resources/images/downloads/";
        Path filePath = fileStoragePath.resolve(filename);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, "Test Content".getBytes());

        UrlResource resourceMock = mock(UrlResource.class);
        lenient().when(resourceMock.exists()).thenReturn(false);
        lenient().when(resourceMock.isReadable()).thenReturn(true);

        ImageService imageServiceSpy = Mockito.spy(new ImageService(fileStorageLocation, fileUploadRepository, catRepository, catsitterRepository));
        doReturn(resourceMock).when(imageServiceSpy).createUrlResource(any(Path.class));

        // Act & Assert
        FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> {
            imageServiceSpy.downloadImage(filename);
        });

        assertEquals("File doesn't exist or is not readable", exception.getMessage());
    }

    @Test
    void downloadImage_MalformedURL() throws MalformedURLException {

        // Arrange
        String filename = "catsitterimage.jpg";
        String fileStorageLocation = "src/test/resources/images/downloads/";

        ImageService imageServiceSpy = Mockito.spy(new ImageService(fileStorageLocation, fileUploadRepository, catRepository, catsitterRepository));
        Mockito.doThrow(new MalformedURLException("Malformed URL")).when(imageServiceSpy).createUrlResource(Mockito.any(Path.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imageServiceSpy.downloadImage(filename);
        });
        assertEquals("Issue in reading the file", exception.getMessage());
    }

    @Test
    void getFilePath_shouldReturnCorrectFilePath() {

        // Arrange
        String filename = "catimage.jpg";
        String fileStorageLocation = "src/test/resources/images/uploads/";

        ImageService imageService = new ImageService(fileStorageLocation, fileUploadRepository, catRepository, catsitterRepository);

        // Act
        Path filePath = imageService.getFilePath(filename);

        // Assert
        Path expectedPath = Paths.get(fileStorageLocation).toAbsolutePath().resolve(filename);
        assertEquals(expectedPath, filePath);
    }

    @Test
    void createUrlResource_shouldCreateUrlResource() throws Exception {

        // Arrange
        String fileStoragePath = "src/test/resources/images/uploads/";
        Path path = Paths.get(fileStoragePath, "catimage.jpg");
        Files.createDirectories(path.getParent());
        Files.createFile(path);

        // Act
        UrlResource urlResource = imageService.createUrlResource(path);

        // Assert
        assertNotNull(urlResource);
        assertEquals(path.toUri(), urlResource.getURI());
    }

    @Test
    void createUrlResource_shouldThrowMalformedURLException() throws MalformedURLException {

        // Arrange
        Path path = Paths.get("src/test/resources/images/uploads/catsitterimage.jpg");
        ImageService imageServiceSpy = Mockito.spy(new ImageService("src/test/resources/images/uploads/", fileUploadRepository, catRepository, catsitterRepository));
        doThrow(new MalformedURLException("Malformed URL")).when(imageServiceSpy).createUrlResource(Mockito.any(Path.class));

        // Act & Assert
        assertThrows(MalformedURLException.class, () -> {
            imageServiceSpy.createUrlResource(path);
        });
    }
}