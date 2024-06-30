package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.LopHoc;
import com.example.springdatajpa.entity.LopHocSinhVien;
import com.example.springdatajpa.entity.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LopHocSinhVienRepository extends JpaRepository<LopHocSinhVien, Integer> {
    List<LopHocSinhVien> findByLopHoc(LopHoc lopHoc);
    LopHocSinhVien findByLopHocAndSinhVien(LopHoc lopHoc,SinhVien sinhVien);
}