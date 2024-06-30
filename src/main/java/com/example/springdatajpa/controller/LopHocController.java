package com.example.springdatajpa.controller;

import com.example.springdatajpa.entity.LopHoc;
import com.example.springdatajpa.repository.GiangVienRepository;
import com.example.springdatajpa.repository.HocKyRepository;
import com.example.springdatajpa.repository.LopHocRepository;
import com.example.springdatajpa.repository.MonHocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/lophoc")
public class LopHocController {

    @Autowired
    private LopHocRepository lopHocRepository;

    @Autowired
    private MonHocRepository monHocRepository;

    @Autowired
    private GiangVienRepository giangVienRepository;

    @Autowired
    private HocKyRepository hocKyRepository;

    @GetMapping("/search")
    public String search(Model model) {
        List<LopHoc> lopHocs = lopHocRepository.findAll();
        model.addAttribute("lopHocList", lopHocs);
        return "lophoc/searchLopHoc";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("monHocs", monHocRepository.findAll());
        model.addAttribute("giangViens", giangVienRepository.findAll());
        model.addAttribute("hocKys", hocKyRepository.findAll());
        return "lophoc/createLopHoc";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute LopHoc lopHoc) {
        lopHocRepository.save(lopHoc);
        return "redirect:/lophoc/search";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        lopHocRepository.deleteById(id);
        return "redirect:/lophoc/search";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam("id") int id) {
        LopHoc lopHoc = lopHocRepository.findById(id).orElse(null);
        if (lopHoc != null) {
            model.addAttribute("lopHoc", lopHoc);
            model.addAttribute("monHocs", monHocRepository.findAll());
            model.addAttribute("giangViens", giangVienRepository.findAll());
            model.addAttribute("hocKys", hocKyRepository.findAll());
            return "lophoc/editLopHoc";
        }
        return "redirect:/lophoc/search";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("lopHoc") LopHoc lopHoc) {
        lopHocRepository.save(lopHoc);
        return "redirect:/lophoc/search";
    }

}
