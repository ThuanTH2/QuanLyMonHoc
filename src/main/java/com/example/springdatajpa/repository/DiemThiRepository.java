package com.example.springdatajpa.repository;


import com.example.springdatajpa.entity.DiemThi;
import com.example.springdatajpa.entity.LopHoc;
import com.example.springdatajpa.entity.LopHocSinhVien;
import com.example.springdatajpa.entity.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DiemThiRepository extends JpaRepository<DiemThi, Integer> {
    List<DiemThi> findByLopHocSinhVien_SinhVien(SinhVien sinhVien);
    DiemThi findByLopHocSinhVien(LopHocSinhVien lopHocSinhVien);
    List<DiemThi> findByLopHocSinhVien_LopHoc_MaLopHoc(Integer maLopHoc);

}

