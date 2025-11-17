package istad.co.nectarapi.features.order.dto;

import istad.co.nectarapi.enums.Status;
import istad.co.nectarapi.features.orderItem.dto.OrderItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        @NotBlank(message = "Order number is required")
        String orderNumber,

        @NotNull(message = "Total price is required")
        @DecimalMin(value = "0.01", message = "Total price must be greater than 0")
        BigDecimal totalPrice,

        @NotNull(message = "Status is required")
        Status status,

        @NotEmpty(message = "Order must have at least one item")
        @Valid
        List<OrderItemRequest> items
) {}
