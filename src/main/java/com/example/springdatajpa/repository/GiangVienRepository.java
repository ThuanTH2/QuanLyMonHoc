package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.GiangVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiangVienRepository extends JpaRepository<GiangVien, Integer> {
    GiangVien findByAccount_Email(String email);

}
