package com.study.ecommerce.domain.cart.service;

import com.study.ecommerce.domain.cart.dto.CartItemRequest;
import com.study.ecommerce.domain.cart.dto.CartItemResponse;
import com.study.ecommerce.domain.cart.dto.CartResponse;

public interface CartService {
    CartResponse getCart(String email);
    CartItemResponse addCartItem(CartItemRequest request, String email);
    CartItemResponse updateCartItem(Long cartItemId, CartItemRequest request, String email);
    void removeCartItem(Long cartItemId, String email);
}
