package istad.co.nectarapi.features.product.dto;

import istad.co.nectarapi.audit.AuditResponse;
import istad.co.nectarapi.domain.Category;
import jakarta.persistence.Column;

import java.math.BigDecimal;

public record ProductResponse (
        String uuid,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        Boolean isActive,
        String category,
        AuditResponse audit
){
}
