package org.cris6h16.facades;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAddressDTO {
    private String street;
    private String city;
    private String state;// todo: renombrar todo a estado(en lugar de provincia)
    private String mobileNumber;
    private String reference;
}
