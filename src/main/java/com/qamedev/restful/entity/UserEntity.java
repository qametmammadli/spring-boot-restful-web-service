package com.qamedev.restful.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity(name = "users")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = -4998945275788963859L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String surName;

    @Column(nullable = false, length = 70, unique = true)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    private String emailVerificationToken;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean emailVerificationStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AddressEntity> addresses;
}

