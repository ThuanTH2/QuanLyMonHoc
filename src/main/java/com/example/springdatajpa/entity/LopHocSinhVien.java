package com.example.springdatajpa.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "LopHoc_SinhVien", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"MaSinhVien", "MaLopHoc"})
})
@Data
public class LopHocSinhVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSinhVienLopHoc")
    private Integer maSinhVienLopHoc;

    @ManyToOne
    @JoinColumn(name = "MaLopHoc", nullable = false)
    private LopHoc lopHoc;

    @ManyToOne
    @JoinColumn(name = "MaSinhVien", nullable = false)
    private SinhVien sinhVien;

    @OneToOne(mappedBy = "lopHocSinhVien", cascade = CascadeType.ALL)
    private DiemThi diemThi;
}
