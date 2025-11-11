package istad.co.nectarapi.features.file;

import istad.co.nectarapi.features.file.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @GetMapping("/{folder}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<FileUploadResponse> getAllFilesByFolder(@PathVariable String folder) {
        log.info("Getting all files from folder: {}", folder);
        return fileStorageService.getAllFilesByFolder(folder);
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<FileUploadResponse> getAllFiles() {
        log.info("Getting all files from bucket");
        return fileStorageService.getAllFiles();
    }

    @PostMapping("/upload/profile")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public FileUploadResponse uploadProfileImage(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileStorageService.uploadFile(file, "profiles");
        String filename = fileStorageService.getFilenameFromUrl(fileUrl);
        return new FileUploadResponse(
                fileUrl,
                filename,
                file.getSize(),
                file.getContentType(),
                "Profile image uploaded successfully"
        );
    }

    @PostMapping("/upload/product")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public FileUploadResponse uploadProductImage(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileStorageService.uploadFile(file, "products");
        String filename = fileStorageService.getFilenameFromUrl(fileUrl);
        return new FileUploadResponse(
                fileUrl,
                filename,
                file.getSize(),
                file.getContentType(),
                "Product image uploaded successfully"
        );
    }

    @PostMapping("/upload/category")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public FileUploadResponse uploadCategoryImage(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileStorageService.uploadFile(file, "categories");
        String filename = fileStorageService.getFilenameFromUrl(fileUrl);
        return new FileUploadResponse(
                fileUrl,
                filename,
                file.getSize(),
                file.getContentType(),
                "Category image uploaded successfully"
        );
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void deleteFile(@RequestParam("fileUrl") String fileUrl) {
        fileStorageService.deleteFile(fileUrl);
    }

    @GetMapping("/exists")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public boolean checkFileExists(@RequestParam("fileUrl") String fileUrl) {
        return fileStorageService.fileExists(fileUrl);
    }
}