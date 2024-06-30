package com.example.springdatajpa.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "DiemThi")
@Data
public class DiemThi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDiemThi")
    private Integer maDiemThi;

    @OneToOne
    @JoinColumn(name = "MaSinhVienLopHoc", nullable = false, unique = true)
    private LopHocSinhVien lopHocSinhVien;

    @Column(name = "DiemQuaTrinh", nullable = false)
    private Float diemQuaTrinh;

    @Column(name = "DiemCuoiKy", nullable = false)
    private Float diemCuoiKy;

    @Column(name = "Khoa", nullable = false)
    private Boolean khoa =false;

    @Column(name = "NgayCapNhat",nullable = false)
    private Date ngayCapNhat;
}
