package com.app.ecom.service;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);
    Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest);
    List<ProductResponse> getAllProducts();
    boolean deleteProduct(Long id);
    List<ProductResponse> searchProduct(String keyword);
}
