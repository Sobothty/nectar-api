package istad.co.nectarapi.features.file.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FileUploadResponse(
        String fileUrl,
        String filename,
        Long fileSize,
        String contentType,
        String message
) {}