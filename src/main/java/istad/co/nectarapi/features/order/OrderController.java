package istad.co.nectarapi.features.order;

import istad.co.nectarapi.features.order.dto.OrderRequest;
import istad.co.nectarapi.features.order.dto.OrderUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return new ResponseEntity<>(Map.of("orders", orderService.getAllOrders()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderRequest orderRequest){
        return new ResponseEntity<>(orderService.createOrder(orderRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateOrder(
            @PathVariable String uuid,
            @RequestBody @Valid OrderUpdate orderUpdate){
        return new ResponseEntity<>(orderService.updateOrder(uuid, orderUpdate), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteOrder(@PathVariable String uuid) {
        return new ResponseEntity<>(orderService.deleteOrder(uuid), HttpStatus.OK);
    }
}
