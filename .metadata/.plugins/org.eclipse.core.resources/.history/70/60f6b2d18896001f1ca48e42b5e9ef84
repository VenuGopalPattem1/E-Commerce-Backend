package com.nt.dto;

import java.time.LocalDate;
import java.util.Set;

import lombok.Data;

@Data
public class OrderDTO {
    private Long id;
    private String orderTrackingId;
    private Integer totalQuantity;
    private Integer totalPrice;
    private String status;
    private LocalDate deliveryDate;
    private CustomerDTO customer;
    private AddressDTO address;
    private Set<OrderItemsDTO> orderItems;
}
