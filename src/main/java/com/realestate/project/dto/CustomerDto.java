package com.realestate.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  //Lombok annotation , used to create setters and getters
@NoArgsConstructor  //Create constructor with empty fields
@AllArgsConstructor  //Create constructor with all fields
public class CustomerDto {  //Dto means data transfer objects
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String propertyType;
    private String address;
    private String password;
}
