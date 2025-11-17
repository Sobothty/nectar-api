package istad.co.nectarapi.features.order.dto;

import istad.co.nectarapi.domain.OrderItem;
import istad.co.nectarapi.enums.Status;
import istad.co.nectarapi.features.orderItem.dto.OrderItemRequest;

import java.math.BigDecimal;
import java.util.List;

public record OrderUpdate(
        String orderNumber,
        BigDecimal totalPrice,
        Status status,
        List<OrderItemRequest> items
) {
}
