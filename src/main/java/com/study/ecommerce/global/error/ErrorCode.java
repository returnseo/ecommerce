package com.study.ecommerce.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    INVALID_TYPE_VALUE(400, "C002", "Invalid Type Value"),
    ACCESS_DENIED(403, "C003", "Access is Denied"),
    RESOURCE_NOT_FOUND(404, "C004", "Resource Not Found"),
    METHOD_NOT_ALLOWED(405, "C005", "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "C006", "Server Error"),

    // Member
    EMAIL_DUPLICATION(400, "M001", "Email is Duplicated"),
    MEMBER_NOT_FOUND(404, "M002", "Member Not Found"),
    PASSWORD_NOT_MATCH(400, "M003", "Password Not Match"),

    // Product
    PRODUCT_NOT_FOUND(404, "P001", "Product Not Found"),
    OUT_OF_STOCK(400, "P002", "Out of Stock"),

    // Order
    ORDER_NOT_FOUND(404, "O001", "Order Not Found"),
    CANNOT_CANCEL_ORDER(400, "O002", "Cannot Cancel Order"),

    // Cart
    CART_NOT_FOUND(404, "CA001", "Cart Not Found"),
    CART_ITEM_NOT_FOUND(404, "CA002", "Cart Item Not Found"),

    // Category
    CATEGORY_NOT_FOUND(404, "CT001", "Category Not Found"),

    // Payment
    PAYMENT_NOT_FOUND(404, "PA001", "Payment Not Found"),
    PAYMENT_FAILED(400, "PA002", "Payment Failed");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
