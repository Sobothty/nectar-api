package istad.co.nectarapi.features.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;

@RestController
@RequestMapping("/nectar")
@RequiredArgsConstructor
@Slf4j
public class FileServingController {

    private final FileStorageService fileStorageService;

    @GetMapping("/{folder}/{filename}")
    public ResponseEntity<InputStreamResource> serveFile(
            @PathVariable String folder,
            @PathVariable String filename
    ) {
        try {
            String objectName = folder + "/" + filename;

            log.info("Serving file: {}", objectName);

            // Get file from MinIO
            InputStream fileStream = fileStorageService.getFile(objectName);

            // Get content type
            String contentType = fileStorageService.getFileContentType(objectName);

            // Create response with proper headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setCacheControl("public, max-age=31536000"); // Cache for 1 year

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(new InputStreamResource(fileStream));

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error serving file: {}/{} - {}", folder, filename, e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "File not found"
            );
        }
    }
}