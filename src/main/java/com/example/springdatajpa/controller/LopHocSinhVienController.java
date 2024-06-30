package com.example.springdatajpa.controller;

import com.example.springdatajpa.entity.LopHoc;
import com.example.springdatajpa.entity.LopHocSinhVien;
import com.example.springdatajpa.entity.SinhVien;
import com.example.springdatajpa.repository.LopHocRepository;
import com.example.springdatajpa.repository.LopHocSinhVienRepository;
import com.example.springdatajpa.repository.SinhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/lophocsinhvien")
public class LopHocSinhVienController {

    @Autowired
    private LopHocRepository lopHocRepository;

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Autowired
    private LopHocSinhVienRepository lopHocSinhVienRepository;

    @GetMapping("/add")
    public String formAdd(Model model) {
        List<LopHoc> lopHocList = lopHocRepository.findAll();
        List<SinhVien> sinhVienList = sinhVienRepository.findAll();
        model.addAttribute("lopHocList", lopHocList);
        model.addAttribute("sinhVienList", sinhVienList);
        return "lophocsinhvien/add";
    }

    @PostMapping("/add")
    public String formAdd(@RequestParam("maLopHoc") Integer maLopHoc,
                                    @RequestParam("maSinhVien") Integer maSinhVien,Model model) {
        LopHoc lopHoc = lopHocRepository.findById(maLopHoc).orElse(null);
        SinhVien sinhVien = sinhVienRepository.findById(maSinhVien).orElse(null);
        LopHocSinhVien existingEntry = lopHocSinhVienRepository.findByLopHocAndSinhVien(lopHoc, sinhVien);
        if (existingEntry != null) {
            // Nếu đã tồn tại, thông báo cho người dùng và chuyển hướng về trang thêm sinh viên
            return "redirect:/lophocsinhvien/add?abc";
        }

        if (lopHoc != null && sinhVien != null) {
            LopHocSinhVien lopHocSinhVien = new LopHocSinhVien();
            lopHocSinhVien.setLopHoc(lopHoc);
            lopHocSinhVien.setSinhVien(sinhVien);
            lopHocSinhVienRepository.save(lopHocSinhVien);
            return "redirect:/lophocsinhvien/add?success";
        } else {
            return "redirect:/lophocsinhvien/add?error";
        }
    }
}
