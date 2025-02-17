package org.cris6h16;

import org.cris6h16.Exception.AddressComponentException;

import java.util.HashSet;
import java.util.Set;

import static org.cris6h16.Exception.AddressErrorCode.COUNTRY_NOT_FOUND;

public final class CountryCity {
    static final Set<CountryInfo> countries = new HashSet<>(1);

    static {
        ProvinceInfo napo = ProvinceInfo.builder()
                .name("Napo")
                .cities(Set.of(
                        new CityInfo("Tena"),
                        new CityInfo("Archidona"),
                        new CityInfo("Carlos Julio Arosemena Tola"),
                        new CityInfo("El Chaco"),
                        new CityInfo("Quijos")))
                .build();



        countries.add(CountryInfo.builder()
                .name("Ecuador")
                .provinces(Set.of(napo))
                .mobileNumberRegex("^\\+5939[0-9]{8}$")
                .build());
    }

    static boolean existCountryByName(String name) {
        return countries.stream().anyMatch(country -> country.getName().equals(name));
    }

    static boolean existStateByName(String provinceName, String countryName) {
        return countries.stream()
                .filter(country -> country.getName().equals(countryName))
                .anyMatch(country -> country.getProvinces().stream()
                        .anyMatch(province -> province.getName().equals(provinceName)));
    }

    static boolean existCityByName(String cityName, String provinceName, String countryName) {
        return countries.stream()
                .filter(country -> country.getName().equals(countryName))
                .anyMatch(country -> country.getProvinces().stream()
                        .filter(province -> province.getName().equals(provinceName))
                        .anyMatch(province -> province.getCities().stream()
                                .anyMatch(city -> city.getName().equals(cityName))));
    }
    static String getRegexByCountry(String countryName) {
        return countries.stream()
                .filter(country -> country.getName().equals(countryName))
                .map(CountryInfo::getMobileNumberRegex)
                .findFirst()
                .orElseThrow(() -> new AddressComponentException(COUNTRY_NOT_FOUND));
    }
}
