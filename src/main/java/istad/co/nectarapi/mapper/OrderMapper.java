package istad.co.nectarapi.mapper;

import istad.co.nectarapi.audit.AuditMapper;
import istad.co.nectarapi.domain.Order;
import istad.co.nectarapi.domain.OrderItem;
import istad.co.nectarapi.features.order.dto.OrderRequest;
import istad.co.nectarapi.features.order.dto.OrderResponse;
import istad.co.nectarapi.features.order.dto.OrderUpdate;
import istad.co.nectarapi.features.orderItem.dto.OrderItemRequest;
import istad.co.nectarapi.features.orderItem.dto.OrderItemResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AuditMapper.class, MapperHelper.class})
public interface OrderMapper {

    // Map Order → OrderResponse
    @Mapping(target = "items", source = "items")
    @Mapping(target = "audit", source = "order")
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "productName", source = "product.name")
    OrderItemResponse toOrderItemResponse(OrderItem item);

    List<OrderItemResponse> toOrderItemResponses(List<OrderItem> items);

    @Mapping(target = "items", source = "items")
    Order fromOrderRequest(OrderRequest orderRequest);

    @Mapping(target = "product", source = "productUuid", qualifiedByName = "toProductName")
    OrderItem toOrderItem(OrderItemRequest itemRequest);

    List<OrderItem> toOrderItems(List<OrderItemRequest> items);

    @Mapping(target = "items", source = "items")
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateOrder(OrderUpdate orderUpdate, @MappingTarget Order order);
}

