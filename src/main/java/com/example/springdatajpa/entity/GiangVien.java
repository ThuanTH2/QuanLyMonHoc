package com.example.springdatajpa.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "GiangVien")
@Data
public class GiangVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaGiangVien")
    private Integer maGiangVien;

    @Column(name = "TenGiangVien", nullable = false)
    private String tenGiangVien;

    @OneToOne
    @JoinColumn(name = "MaAccount", nullable = false, unique = true)
    private Account account;
}
