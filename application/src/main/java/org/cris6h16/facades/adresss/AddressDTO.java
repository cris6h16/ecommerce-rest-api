package org.cris6h16.facades.adresss;

import lombok.Builder;

@Builder
public class AddressDTO {
    private Long id;
    private String street;
    private String reference;
    private String city;
    private String state;
    private String country;
    private String mobileNumber;
    private Long userId;
}
