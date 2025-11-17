package istad.co.nectarapi.features.order;

import istad.co.nectarapi.base.BasedMessage;
import istad.co.nectarapi.features.order.dto.OrderRequest;
import istad.co.nectarapi.features.order.dto.OrderResponse;
import istad.co.nectarapi.features.order.dto.OrderUpdate;

import java.util.List;

public interface OrderService {

    List<OrderResponse> getAllOrders();

    OrderResponse createOrder(OrderRequest orderRequest);

    OrderResponse updateOrder(String uuid, OrderUpdate orderUpdate);

    BasedMessage deleteOrder(String uuid);
}
