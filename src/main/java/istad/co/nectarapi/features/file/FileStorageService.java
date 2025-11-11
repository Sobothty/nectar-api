package istad.co.nectarapi.features.file;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import istad.co.nectarapi.features.file.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${app.base-url}")
    private String appBaseUrl;

    @Value("${app.file-path}")
    private String filePath;

    public List<FileUploadResponse> getAllFilesByFolder(String folder) {
        List<FileUploadResponse> files = new ArrayList<>();

        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(folder + "/")
                            .recursive(true)
                            .build()
            );

            for (Result<Item> result : results) {
                Item item = result.get();

                // Skip directories
                if (!item.isDir()) {
                    String fileUrl = appBaseUrl + filePath + "/" + item.objectName();
                    String filename = getFilenameFromObjectName(item.objectName());
                    String contentType = getFileContentType(item.objectName());

                    files.add(new FileUploadResponse(
                            fileUrl,
                            filename,
                            item.size(),
                            contentType,
                            null // No message for list
                    ));
                }
            }

            log.info("Found {} files in folder: {}", files.size(), folder);
            return files;

        } catch (Exception e) {
            log.error("Error listing files from folder {}: {}", folder, e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to list files: " + e.getMessage()
            );
        }
    }

    /**
     * Get all files metadata from entire bucket
     */
    public List<FileUploadResponse> getAllFiles() {
        List<FileUploadResponse> files = new ArrayList<>();

        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .recursive(true)
                            .build()
            );

            for (Result<Item> result : results) {
                Item item = result.get();

                // Skip directories
                if (!item.isDir()) {
                    String fileUrl = appBaseUrl + filePath + "/" + item.objectName();
                    String filename = getFilenameFromObjectName(item.objectName());
                    String contentType = getFileContentType(item.objectName());

                    files.add(new FileUploadResponse(
                            fileUrl,
                            filename,
                            item.size(),
                            contentType,
                            null // No message for list
                    ));
                }
            }

            log.info("Found {} total files in bucket", files.size());
            return files;

        } catch (Exception e) {
            log.error("Error listing all files: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to list files: " + e.getMessage()
            );
        }
    }

    private String getFilenameFromObjectName(String objectName) {
        int lastSlashIndex = objectName.lastIndexOf("/");
        return lastSlashIndex != -1 ? objectName.substring(lastSlashIndex + 1) : objectName;
    }

    public String uploadFile(MultipartFile file, String folder) {
        validateFile(file);

        try {
            // Generate UUID filename with original extension
            String extension = getFileExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID().toString() + extension;
            String objectName = folder + "/" + filename;

            // Upload file
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            inputStream.close();

            log.info("File uploaded successfully: {}", objectName);

            return appBaseUrl + filePath + "/" + objectName;

        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to upload file: " + e.getMessage()
            );
        }
    }

    public InputStream getFile(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error retrieving file: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "File not found"
            );
        }
    }

    public String getFileContentType(String objectName) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return stat.contentType();
        } catch (Exception e) {
            log.error("Error getting file metadata: {}", e.getMessage());
            return "application/octet-stream"; // Default content type
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            // Extract object name from URL
            String objectName = extractObjectName(fileUrl);

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );

            log.info("File deleted successfully: {}", objectName);

        } catch (Exception e) {
            log.error("Error deleting file: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to delete file: " + e.getMessage()
            );
        }
    }


    public boolean fileExists(String fileUrl) {
        try {
            String objectName = extractObjectName(fileUrl);
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "File is required"
            );
        }

        // Check file size (10MB max)
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxSize) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "File size exceeds maximum limit of 10MB"
            );
        }

        // Check file type (images only)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only image files are allowed (JPEG, PNG, GIF, WebP)"
            );
        }

        // Validate file extension
        String extension = getFileExtension(file.getOriginalFilename());
        if (!isValidImageExtension(extension)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid file extension. Allowed: .jpg, .jpeg, .png, .gif, .webp"
            );
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Filename is empty"
            );
        }

        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "File must have an extension"
            );
        }

        return filename.substring(lastDotIndex).toLowerCase();
    }


    private boolean isValidImageExtension(String extension) {
        return extension.matches("\\.(jpg|jpeg|png|gif|webp)");
    }

    private String extractObjectName(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "File URL is required"
            );
        }

        String pathPrefix = filePath + "/";
        int pathIndex = fileUrl.indexOf(pathPrefix);

        if (pathIndex == -1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid file URL format"
            );
        }

        return fileUrl.substring(pathIndex + pathPrefix.length());
    }


    public String getFilenameFromUrl(String fileUrl) {
        String objectName = extractObjectName(fileUrl);
        int lastSlashIndex = objectName.lastIndexOf("/");
        return lastSlashIndex != -1 ? objectName.substring(lastSlashIndex + 1) : objectName;
    }
}