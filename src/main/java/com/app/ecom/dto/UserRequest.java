package com.app.ecom.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String role;
    private AddressDTO address;
}
