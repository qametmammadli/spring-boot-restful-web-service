package com.qamedev.restful.entity;

import com.qamedev.restful.dto.TokenType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "token")
@Data
public class TokenEntity implements Serializable {

    private static final long serialVersionUID = 3036184765009770618L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 25)
    @Enumerated(EnumType.STRING)
    private TokenType name;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
