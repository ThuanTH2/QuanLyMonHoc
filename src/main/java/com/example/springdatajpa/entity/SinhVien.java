package com.example.springdatajpa.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "SinhVien")
@Data
public class SinhVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSinhVien")
    private Integer maSinhVien;

    @Column(name = "TenSinhVien", nullable = false)
    private String tenSinhVien;

    @OneToOne
    @JoinColumn(name = "MaAccount", nullable = false, unique = true)
    private Account account;
}
