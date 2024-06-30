package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.MonHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonHocRepository extends JpaRepository<MonHoc, Integer> {

}
