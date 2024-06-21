package nl.novi.catsittermanager.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.exceptions.InvalidTypeException;
import nl.novi.catsittermanager.models.ImageUpload;
import nl.novi.catsittermanager.services.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/{type}/{id}/images")
    public ResponseEntity<String> uploadImage(@PathVariable String type, @PathVariable String id, @RequestParam("file") MultipartFile file) {
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{type}/{id}/images/uploads")
                .path(Objects.requireNonNull(file.getOriginalFilename())).toUriString();
        String contentType = file.getContentType();
        ImageUpload imageUpload;

        if (type.equals("cat")) {
            UUID catId = UUID.fromString(id);
            imageUpload = imageService.uploadCatImage(catId, file);
        } else if (type.equals("catsitter")) {
            imageUpload = imageService.uploadCatsitterImage(id, file);
        } else {
            throw new InvalidTypeException(type);
        }
        return ResponseEntity.ok().body("Image uploaded");
    }

    @GetMapping("/{type}/{id}/images/{filename}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String type, @PathVariable String id, @PathVariable String filename, HttpServletRequest request) throws FileNotFoundException {
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
}
