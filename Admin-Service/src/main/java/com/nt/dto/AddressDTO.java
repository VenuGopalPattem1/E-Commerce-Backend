package com.nt.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private Long id; // Add an ID if necessary for updates
    private String street;
    private String city;
    private String state;
    private String zipCode;
}
