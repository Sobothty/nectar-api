package istad.co.nectarapi.features.orderItem.dto;

import java.math.BigDecimal;

public record OrderItemResponse (
        String uuid,
        String productName,
        Integer quantity,
        BigDecimal price
){
}
