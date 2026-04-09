package com.app.ecom.service.impl;

import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.dto.OrderItemDTO;
import com.app.ecom.dto.OrderResponse;
import com.app.ecom.entity.CartItem;
import com.app.ecom.entity.Order;
import com.app.ecom.entity.OrderItem;
import com.app.ecom.entity.User;
import com.app.ecom.enums.OrderStatus;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import com.app.ecom.service.CartItemService;
import com.app.ecom.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final CartItemService cartService;

    @Override
    public Optional<OrderResponse> createOrder(String userId) {

        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()) return Optional.empty();

        List<CartItemResponse> cartItems = cartService.getCartItemsForUser(userId);
        if(cartItems.isEmpty()) return Optional.empty();

        User user = userOpt.get();

        BigDecimal totalPrice = cartItems.stream()
                .map(cartItem -> BigDecimal.valueOf(Double.parseDouble(cartItem.getTotalPrice()))
                        .multiply(BigDecimal.valueOf(Long.parseLong(cartItem.getQuantity()))))
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalPrice(totalPrice);

        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProduct(),
                        Integer.parseInt(item.getQuantity()),
                        BigDecimal.valueOf(Double.parseDouble(item.getTotalPrice())),
                        order
                ))
                .toList();

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order){
        return new OrderResponse(
                order.getId(),
                order.getTotalPrice(),
                order.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProduct().getId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                        ))
                        .collect(Collectors.toList()),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
