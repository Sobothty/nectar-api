package istad.co.nectarapi.features.product.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductRequest(

        @NotBlank(message = "Category UUID is required")
        String categoryUuid,

        @NotBlank(message = "Product name is required")
        @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "Description is required")
        @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
        BigDecimal price,

        @NotBlank(message = "Image URL is required")
        @Pattern(
                regexp = "^(https?://).+",
                message = "Image URL must be a valid URL starting with http or https"
        )
        String imageUrl,

        @NotNull(message = "isActive status is required")
        Boolean isActive
) {
}
