package com.example.laptopshop.dto;

public class CartAddRequest {
    private Long productId;
    private int quantity;

    // Constructor mặc định (nên có cho Spring)
    public CartAddRequest() { }

    // Getter/Setter
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
