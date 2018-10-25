package com.pan.file.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by FantasticPan on 2018/1/24.
 * 对象类
 */
@Entity
@Table(name = "member")
@Setter
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    String username;
    String password;

    public Member() {}

    public Member(String username,String password) {
        this.username = username;
        this.password = password;
    }
}
