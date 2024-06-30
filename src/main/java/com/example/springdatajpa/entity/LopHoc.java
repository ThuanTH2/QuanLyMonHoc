package com.example.springdatajpa.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "LopHoc")
@Data
public class LopHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maLopHoc;

    @Column(nullable = false)
    private String tenLopHoc;

    @ManyToOne
    @JoinColumn(name = "MaMonHoc")
    private MonHoc monHoc;

    @ManyToOne
    @JoinColumn(name = "MaGiangVien")
    private GiangVien giangVien;

    @ManyToOne
    @JoinColumn(name = "MaHocKy")
    private HocKy hocKy;

}

