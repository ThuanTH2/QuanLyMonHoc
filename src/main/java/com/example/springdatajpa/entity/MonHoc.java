package com.example.springdatajpa.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "MonHoc")
@Data
public class MonHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaMonHoc")
    private Integer maMonHoc;

    @Column(name = "TenMonHoc", nullable = false)
    private String tenMonHoc;

    @Column(name = "HeSo", nullable = false)
    private Float heSo;
}
