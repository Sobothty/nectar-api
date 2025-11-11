package istad.co.nectarapi.features.category.dto;

import istad.co.nectarapi.audit.AuditResponse;

public record CategoryResponse (
        String name,
        String description,
        AuditResponse audit
) {
}
