package istad.co.nectarapi.features.orderItem.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record OrderItemRequest (
        @NotBlank(message = "Product UUID is required")
        String productUuid,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        BigDecimal price
) {}
