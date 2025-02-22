package org.cris6h16;

import org.cris6h16.Exception.AddressComponentException;
import org.springframework.stereotype.Component;

import static org.cris6h16.CountryCity.existCityByName;
import static org.cris6h16.CountryCity.existCountryByName;
import static org.cris6h16.CountryCity.existStateByName;
import static org.cris6h16.CountryCity.getRegexByCountry;
import static org.cris6h16.Exception.AddressErrorCode.INVALID_CITY;
import static org.cris6h16.Exception.AddressErrorCode.INVALID_COUNTRY;
import static org.cris6h16.Exception.AddressErrorCode.INVALID_MOBILE_NUMBER;
import static org.cris6h16.Exception.AddressErrorCode.INVALID_REFERENCE;
import static org.cris6h16.Exception.AddressErrorCode.INVALID_STATE;
import static org.cris6h16.Exception.AddressErrorCode.INVALID_STREET;

// todo: mejorar validación con ISO 3166-1 alpha-2
@Component
class AddressValidator {


    void validate(UpdateAddressInput input) {
        validateCountry(input.getCountry());
        validateState(input.getState(), input.getCountry());
        validateCity(input.getCity(), input.getState(), input.getCountry());
        validateMobileNumber(input.getMobileNumber(), input.getCountry());
        validateStreet(input.getStreet());
        validateReference(input.getReference());
    }

    void validate(CreateAddressInput input) {
        validateCountry(input.getCountry());
        validateState(input.getState(), input.getCountry());
        validateCity(input.getCity(), input.getState(), input.getCountry());
        validateMobileNumber(input.getMobileNumber(), input.getCountry());
        validateStreet(input.getStreet());
        validateReference(input.getReference());
    }

    private void validateReference(String reference) {
        if (reference == null || reference.isBlank()) {
            throw new AddressComponentException(INVALID_REFERENCE);
        }
    }

    private void validateStreet(String street) {
        if (street == null || street.isBlank()) {
            throw new AddressComponentException(INVALID_STREET);
        }
    }

    private void validateMobileNumber(String mobileNumber, String country) {
        String regex = getRegexByCountry(country);
        if (mobileNumber == null || !mobileNumber.matches(regex)) {
            throw new AddressComponentException(INVALID_MOBILE_NUMBER);
        }
    }

    private void validateState(String provinceName, String countryName) {
        if (provinceName == null || !existStateByName(provinceName, countryName)) {
            throw new AddressComponentException(INVALID_STATE);
        }
    }

    private void validateCountry(String name) {
        if (name == null || !existCountryByName(name)) {
            throw new AddressComponentException(INVALID_COUNTRY);
        }
    }

    private void validateCity(String city, String state, String country){
        if (city == null || !existCityByName(city, state, country)) {
            throw new AddressComponentException(INVALID_CITY);
        }
    }


}
