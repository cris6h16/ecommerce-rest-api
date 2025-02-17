package org.cris6h16;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "addresses")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String street;

    @Column(length = 255, nullable = false)
    private String reference;

    @Column(length = 100, nullable = false)
    private String city;

    @Column(length = 100,nullable = false)
    private String state;

    @Column(length = 50, nullable = false)
    private String country;

    @Column(name = "mobile_number", length = 13, nullable = false)
    private String mobileNumber;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
