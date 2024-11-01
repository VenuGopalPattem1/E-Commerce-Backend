package com.nt.dto;

import lombok.Data;

@Data
public class OrderItemsDTO {
    private Long id;
    private String imageUrl;
    private Integer unitPrice;
    private Integer quantity;
    private Long productId;
}
