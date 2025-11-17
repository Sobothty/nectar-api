package istad.co.nectarapi.features.order;

import istad.co.nectarapi.base.BasedMessage;
import istad.co.nectarapi.domain.Order;
import istad.co.nectarapi.domain.User;
import istad.co.nectarapi.features.order.dto.OrderRequest;
import istad.co.nectarapi.features.order.dto.OrderResponse;
import istad.co.nectarapi.features.order.dto.OrderUpdate;
import istad.co.nectarapi.features.user.UserRepository;
import istad.co.nectarapi.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(orderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Get authenticated user's email/phone from security context
        String authenticatedUserEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        // Find the user in database
        User user = userRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
        Order order = orderMapper.fromOrderRequest(orderRequest);
        order.setUuid(UUID.randomUUID().toString());
        order.setIsDeleted(false);
        order.setUser(user);
        order = orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse updateOrder(String uuid, OrderUpdate orderUpdate) {
        Order order = orderRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")
        );
        orderMapper.updateOrder(orderUpdate, order);
        order = orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public BasedMessage deleteOrder(String uuid) {
        if (!orderRepository.existsByUuid(uuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        orderRepository.deleteSoftByUuid(uuid);
        return new BasedMessage("Order deleted successfully");
    }
}
