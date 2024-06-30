package com.example.springdatajpa.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Year;

@Entity
@Data
@Table(name = "HocKy")
public class HocKy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHocKy")
    private Integer maHocKy;

    @Column(name = "TenHocKy", nullable = false)
    private String tenHocKy;

    @Column(name = "NamHoc", nullable = false)
    private Year namHoc;

}

