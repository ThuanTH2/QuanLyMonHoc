package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.GiangVien;
import com.example.springdatajpa.entity.LopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LopHocRepository extends JpaRepository<LopHoc, Integer> {
//    List<LopHoc> findByHocKy(int hocKyId);
    List<LopHoc> findByGiangVien(GiangVien giangVien);


}
