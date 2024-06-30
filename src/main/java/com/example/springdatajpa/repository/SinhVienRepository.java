package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SinhVienRepository extends JpaRepository<SinhVien, Integer> {
    SinhVien findByAccount_Email(String email);
}
