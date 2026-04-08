package com.app.ecom.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private String street;
    private String state;
    private String city;
    private String country;
    private String zipCode;
}
