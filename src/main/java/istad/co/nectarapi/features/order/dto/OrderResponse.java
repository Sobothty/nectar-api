package istad.co.nectarapi.features.order.dto;

import istad.co.nectarapi.audit.AuditResponse;
import istad.co.nectarapi.domain.OrderItem;
import istad.co.nectarapi.enums.Status;
import istad.co.nectarapi.features.orderItem.dto.OrderItemResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse (
        String uuid,
        String orderNumber,
        BigDecimal totalPrice,
        Status status,
        List<OrderItemResponse> items,
        AuditResponse audit
) {
}
