package nl.novi.catsittermanager.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.models.FileUploadResponse;
import nl.novi.catsittermanager.services.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    FileUploadResponse singleFileUpload(@RequestParam("file") MultipartFile file) {
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/upload/")
                .path(Objects.requireNonNull(file.getOriginalFilename())).toUriString();
        String contentType = file.getContentType();
        String fileName = imageService.storeFile(file, url);
        return new FileUploadResponse(fileName, contentType, url);
    }

    @PostMapping("/cat/{id}/upload")
    public ResponseEntity<FileUploadResponse> uploadCatImage(@PathVariable("id") UUID catId, @RequestParam("file") MultipartFile file) {
        FileUploadResponse catImage = imageService.uploadCatImage(catId, file);
        return ResponseEntity.ok(catImage);
    }

    @PostMapping(value = "/cat/{id}/image")
    public void assignImageToCat(@PathVariable("id") UUID catId, @RequestParam("file") MultipartFile file) {
        FileUploadResponse catImage = imageService.uploadCatImage(catId, file);
    }

    @PostMapping("/catsitter/{username}/upload")
    public ResponseEntity<FileUploadResponse> uploadCatsitterImage(@PathVariable("username") String username, @RequestParam("file") MultipartFile file) {
        FileUploadResponse catsitterImage = imageService.uploadCatsitterImage(username, file);
        return ResponseEntity.ok(catsitterImage);
    }

    @PostMapping(value = "catsitter/{username}/image")
    public void assignImageToCatsitter(@PathVariable("id") String username, @RequestParam("file") MultipartFile file) {
        FileUploadResponse catsitterImage = imageService.uploadCatsitterImage(username, file);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadSingleFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = imageService.downloadSingleFile(fileName);
        String mimeType;
        try {
            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename()).body(resource);
    }
}
