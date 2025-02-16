package com.nt.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    private Integer unitPrice;
    private Integer quantity;
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "orderId")
//    @JsonIgnore // Prevent circular reference during serialization
    @JsonBackReference // Breaks recursion during serialization
    private Order order;
}
