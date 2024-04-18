package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.ImageUpload;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CatsitterRepository;
import nl.novi.catsittermanager.repositories.FileUploadRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
        Optional<Cat> optionalCat = catRepository.findById(catId);
        Cat cat = optionalCat.orElseThrow(() -> new RecordNotFoundException("Cat not found with id: " + catId));
        String fileName = file.getOriginalFilename();
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/cat/")
                .path(catId.toString())
                .path("/upload/")
                .path(fileName)
                .toUriString();
        String storedFileName = storeFile(file, url);
        ImageUpload imageUpload = fileUploadRepository.save(new ImageUpload(storedFileName, file.getContentType(), url));
        cat.setImage(imageUpload);
        imageUpload.setCat(cat);
        assignImageToCat(imageUpload, catId);
        catRepository.save(cat);
        return imageUpload;
    }

    public ImageUpload uploadCatsitterImage(String username, MultipartFile file) {
        Optional<Catsitter> optionalCatsitter = catsitterRepository.findById(username);
        Catsitter catsitter = optionalCatsitter.orElseThrow(() -> new RecordNotFoundException("Catsitter not found with id: " + username));
        String fileName = file.getOriginalFilename();
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/catsitter/")
                .path(username)
                .path("/upload/")
                .path(fileName)
                .toUriString();
        String storedFileName = storeFile(file, url);
        ImageUpload imageUpload = fileUploadRepository.save(new ImageUpload(storedFileName, file.getContentType(), url));
        catsitter.setImage(imageUpload);
        imageUpload.setCatsitter(catsitter);
        assignImageToCatsitter(imageUpload, username);
        catsitterRepository.save(catsitter);
        return imageUpload;
    }

    public String storeFile(MultipartFile file, String url) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = Paths.get(fileStoragePath + "/" + fileName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in storing the file", e);
        }
        fileUploadRepository.save(new ImageUpload(fileName, file.getContentType(), url));
        return fileName;
    }

    private void assignImageToCat(ImageUpload imageUpload, UUID catId) {
        Optional<Cat> optionalCat = catRepository.findById(catId);
        if (optionalCat.isPresent()) {
            Cat cat = optionalCat.get();
            cat.setImage(imageUpload);
            catRepository.save(cat);
        } else {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No cat found with this id.");
        }
    }

    private void assignImageToCatsitter(ImageUpload imageUpload, String username) {
        Optional<Catsitter> optionalCatsitter = catsitterRepository.findById(username);
        if (optionalCatsitter.isPresent()) {
            Catsitter catsitter = optionalCatsitter.get();
            catsitter.setImage(imageUpload);
            catsitterRepository.save(catsitter);
        } else {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No catsitter found with this username.");
        }
    }

    public Resource downloadImage(String fileName) {
        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading the file", e);
        }
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("File doesn't exist or is not readable");
        }
    }
}
