package com.example.laptopshop.dto;

public class CartUpdateRequest {
    private Long productId;
    private int quantity;

    public CartUpdateRequest() { }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
