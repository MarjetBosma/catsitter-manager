package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.ImageUpload;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CatsitterRepository;
import nl.novi.catsittermanager.repositories.FileUploadRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${my.upload_location}")
    private final Path fileStoragePath;
    private final String fileStorageLocation;
    private final FileUploadRepository fileUploadRepository;
    private final CatRepository catRepository;
    private final CatsitterRepository catsitterRepository;

    public ImageService(@Value("${my.upload_location}") String fileStorageLocation, FileUploadRepository fileUploadRepository, CatRepository catRepository, CatsitterRepository catsitterRepository) {
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        this.fileStorageLocation = fileStorageLocation;
        this.fileUploadRepository = fileUploadRepository;
        this.catRepository = catRepository;
        this.catsitterRepository = catsitterRepository;
        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }
    }

    public ImageUpload uploadCatImage(UUID catId, MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new IllegalArgumentException("File must have an original filename");
        } else {
            Cat cat = catRepository.findById(catId).orElseThrow(() -> new RecordNotFoundException("Cat not found with id: " + catId));

            String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/cat/")
                    .path(catId.toString())
                    .path("/upload/")
                    .path(filename)
                    .toUriString();
            String storedFileName = storeFile(file, url);
            ImageUpload imageUpload = fileUploadRepository.save(new ImageUpload(storedFileName, file.getContentType(), url));
            cat.setImage(imageUpload);
            imageUpload.setCat(cat);
            assignImageToCat(imageUpload, catId);
            catRepository.save(cat);
            return imageUpload;
        }
    }

    public ImageUpload uploadCatsitterImage(String username, MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new IllegalArgumentException("File must have an original filename");
        } else {

            Catsitter catsitter = catsitterRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("Catsitter not found with id: " + username));

            String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/catsitter/")
                    .path(username)
                    .path("/upload/")
                    .path(filename)
                    .toUriString();
            String storedFileName = storeFile(file, url);
            ImageUpload imageUpload = fileUploadRepository.save(new ImageUpload(storedFileName, file.getContentType(), url));
            catsitter.setImage(imageUpload);
            imageUpload.setCatsitter(catsitter);
            assignImageToCatsitter(imageUpload, username);
            catsitterRepository.save(catsitter);
            return imageUpload;
        }
    }

    public String storeFile(MultipartFile file, String url) {
        String filename = FilenameUtils.normalize(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = Paths.get(fileStoragePath + "/" + filename);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in storing the file", e);
        }
        fileUploadRepository.save(new ImageUpload(filename, file.getContentType(), url));
        return filename;
    }

    void assignImageToCat(ImageUpload imageUpload, UUID catId) {
        Optional<Cat> optionalCat = catRepository.findById(catId);
        if (optionalCat.isPresent()) {
            Cat cat = optionalCat.get();
            cat.setImage(imageUpload);
            catRepository.save(cat);
        } else {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No cat found with this id.");
        }
    }

    void assignImageToCatsitter(ImageUpload imageUpload, String username) {
        Optional<Catsitter> optionalCatsitter = catsitterRepository.findById(username);
        if (optionalCatsitter.isPresent()) {
            Catsitter catsitter = optionalCatsitter.get();
            catsitter.setImage(imageUpload);
            catsitterRepository.save(catsitter);
        } else {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No catsitter found with this username.");
        }
    }

    public Resource downloadImage(String filename) {
        Path path = getFilePath(filename);
        Resource resource;
        try {
            resource = createUrlResource(path);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading the file", e);
        }
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("File doesn't exist or is not readable");
        }
    }

    public Path getFilePath(String filename) {
        return Paths.get(fileStorageLocation).toAbsolutePath().resolve(filename);
    }

    public UrlResource createUrlResource(Path path) throws MalformedURLException {
        return new UrlResource(path.toUri());
    }
}
