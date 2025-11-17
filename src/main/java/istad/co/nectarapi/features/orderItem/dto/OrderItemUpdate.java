package istad.co.nectarapi.features.orderItem.dto;

import java.math.BigDecimal;

public record OrderItemUpdate (
        String productUuid,
        Integer quantity,
        BigDecimal price
) {
}
