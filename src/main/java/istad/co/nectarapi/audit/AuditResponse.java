package istad.co.nectarapi.audit;

import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;

@Builder
public record AuditResponse(
        String createdBy,
        String updatedBy,
        Instant createdAt,
        Instant updatedAt
) {}
