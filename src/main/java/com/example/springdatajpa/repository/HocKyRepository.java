package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.HocKy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HocKyRepository extends JpaRepository<HocKy, Integer> {
}
