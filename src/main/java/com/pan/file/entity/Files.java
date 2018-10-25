package com.pan.file.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by 李攀 on 2017/12/5.
 */
@Entity
@Table(name = "files")
@Getter
@Setter
@ToString
public class Files implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String url;
    private Timestamp date;
}
