package istad.co.nectarapi.features.category.dto;

import istad.co.nectarapi.audit.AuditResponse;

public record CategoryResponse (
        String uuid,
        String name,
        String description,
        AuditResponse audit
) {
}
