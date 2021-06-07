package com.qamedev.restful.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "addresses")
public class AddressEntity implements Serializable {
    private static final long serialVersionUID = -871533680766204061L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String addressId;

    @Column(length = 25, nullable = false)
    private String country;

    @Column(length = 25, nullable = false)
    private String city;

    @Column(name = "address_name", length = 50, nullable = false)
    private String addressName;

    @Column(name = "postal_code", length = 15, nullable = false)
    private String postalCode;

    @Column(name = "address_type", length = 20, nullable = false)
    private String addressType;

    @ManyToOne
    @JoinColumn(columnDefinition = "user_id", nullable = false)
    private UserEntity user;


    @Override
    public String toString() {
        return "AddressEntity{" +
                "id=" + id +
                ", addressId='" + addressId + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", addressName='" + addressName + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", addressType='" + addressType + '\'' +
                '}';
    }
}
