package com.nt.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
    private String password; // Consider excluding sensitive fields
    private String phno;
}
