package com.app.ecom.dto;

import com.app.ecom.entity.Product;
import lombok.Data;

@Data
public class CartItemResponse {
    private String id;
    private Product product;
    private String quantity;
    private String totalPrice;
}
