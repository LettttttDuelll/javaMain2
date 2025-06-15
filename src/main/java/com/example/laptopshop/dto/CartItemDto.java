package com.example.laptopshop.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemDto {
    // GETTER & SETTER
    private Long id;
    private String laptopName;
    private String image;
    private double price;
    private int quantity;

}
