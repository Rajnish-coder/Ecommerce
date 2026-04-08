package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.dto.CartItemResponse;

import java.util.List;

public interface CartItemService {
    boolean addToCart(String userId, CartItemRequest request);
    boolean removeItemFromCart(String userId, Long productId);
    List<CartItemResponse> getCartItemsForUser(String userId);
}
