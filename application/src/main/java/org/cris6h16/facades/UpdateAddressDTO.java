package org.cris6h16.facades;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAddressDTO {
    private String street;
    private String city;
    private String state;
    private String country;
    private String mobileNumber;
}
