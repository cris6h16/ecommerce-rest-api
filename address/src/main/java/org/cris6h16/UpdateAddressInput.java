package org.cris6h16;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateAddressInput implements Prepareable {
    private String street;
    private String city;
    private String state;
    private String country;
    private String mobileNumber;
    private String reference;

    @Override
    public void trim() {
        street = street.trim();
        city = city.trim();
        state = state.trim();
        country = country.trim();
        mobileNumber = mobileNumber.trim();
        reference = reference.trim();
    }

    @Override
    public void nullToEmpty() {
        street = street == null ? "" : street;
        city = city == null ? "" : city;
        state = state == null ? "" : state;
        country = country == null ? "" : country;
        mobileNumber = mobileNumber == null ? "" : mobileNumber;
        reference = reference == null ? "" : reference;
    }
}
