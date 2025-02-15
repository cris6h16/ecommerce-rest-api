package org.cris6h16;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressOutput {
    private Long id;
    private String street;
    private String reference;
    private String city;
    private String state;
    private String country;
    private String mobileNumber;
    private Long userId;
}
