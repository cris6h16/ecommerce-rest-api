package org.cris6h16;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
class CountryInfo {
    private String name;
    private String mobileNumberRegex;
    private Set<ProvinceInfo> provinces;

}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ProvinceInfo {
    private String name;
    private Set<CityInfo> cities;
}

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class CityInfo {
    private String name;
}
