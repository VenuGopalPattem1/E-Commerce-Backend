package com.nt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor
public class OrderItemsDTO {
//    private Long id;
    private String imageUrl;
    private Integer unitPrice;
    private Integer quantity;
    private Long productId;
}
