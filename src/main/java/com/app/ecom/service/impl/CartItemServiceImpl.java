package com.app.ecom.service.impl;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.entity.CartItem;
import com.app.ecom.entity.Product;
import com.app.ecom.entity.User;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import com.app.ecom.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    @Override
    public boolean addToCart(String userId, CartItemRequest request) {

        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if(productOpt.isEmpty()) return false;
        Product product = productOpt.get();
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if(product.getStockQuantity() < request.getQuantity()) return false;
        if(userOpt.isEmpty()) return false;
        User user = userOpt.get();
        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user,product);
        if(existingCartItem != null){
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        }
        else{
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    @Override
    public boolean removeItemFromCart(String userId, Long productId) {

        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()) return false;
        Optional<Product> productOpt = productRepository.findById(productId);
        if(productOpt.isEmpty()) return false;
        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(userOpt.get(),productOpt.get());
        if(existingCartItem == null) return false;
        cartItemRepository.delete(existingCartItem);
        return true;
    }

    @Override
    public List<CartItemResponse> getCartItemsForUser(String userId) {
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()){
            return null;
        }
        User user = userOpt.get();
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        return cartItems.stream()
                .map(this::mapToCartItemResponse).collect(Collectors.toList());
    }


    private CartItemResponse mapToCartItemResponse(CartItem cartItem){
        CartItemResponse response = new CartItemResponse();
        response.setId(cartItem.getId().toString());
        response.setProductName(cartItem.getProduct().getName());
        response.setQuantity(cartItem.getQuantity().toString());
        response.setTotalPrice(cartItem.getPrice().toString());
        return response;
    }
}
