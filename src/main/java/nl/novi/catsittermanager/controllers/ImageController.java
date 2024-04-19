package nl.novi.catsittermanager.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.models.ImageUpload;
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

    @PostMapping("/cat/{id}/upload")
    public ResponseEntity<String> uploadCatImage(@PathVariable("id") UUID catId, @RequestParam("file") MultipartFile file) {
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/upload/")
                .path(Objects.requireNonNull(file.getOriginalFilename())).toUriString();
        String contentType = file.getContentType();
        ImageUpload catImage = imageService.uploadCatImage(catId, file);
        return ResponseEntity.ok().body("Image uploaded");
    }

    @PostMapping("/catsitter/{username}/upload")
    public ResponseEntity<String> uploadCatsitterImage(@PathVariable("username") String username, @RequestParam("file") MultipartFile file) {
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/upload/")
                .path(Objects.requireNonNull(file.getOriginalFilename())).toUriString();
        String contentType = file.getContentType();
        ImageUpload catsitterImage = imageService.uploadCatsitterImage(username, file);
        return ResponseEntity.ok().body("Image uploaded");
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String filename, HttpServletRequest request) {
        Resource resource = imageService.downloadImage(filename);
        String mimeType;
        try {
            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + resource.getFilename()).body(resource);
    }

    @PostMapping(value = "/cat/{id}/image")
    public void assignImageToCat(@PathVariable("id") UUID catId, @RequestParam("file") MultipartFile file) {
        ImageUpload catImage = imageService.uploadCatImage(catId, file);
    }

    @PostMapping(value = "catsitter/{username}/image")
    public void assignImageToCatsitter(@PathVariable("id") String username, @RequestParam("file") MultipartFile file) {
        ImageUpload catsitterImage = imageService.uploadCatsitterImage(username, file);
    }
}
