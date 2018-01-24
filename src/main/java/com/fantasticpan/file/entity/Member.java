package com.fantasticpan.file.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by FantasticPan on 2018/1/24.
 */
@Entity
@Table(name = "member")
@Setter
@Getter
public class Member {

    @Id
    @GeneratedValue
    private Integer id;

    String username;
    String password;

    public Member() {}

    public Member(String username,String password) {
        this.username = username;
        this.password = password;
    }
}
