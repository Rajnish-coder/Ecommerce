package com.app.ecom.dto;

import lombok.Data;

@Data
public class CartItemResponse {
    private String id;
    private String productName;
    private String quantity;
    private String totalPrice;
}
