package com.example.springdatajpa.controller;
import com.example.springdatajpa.entity.MonHoc;
import com.example.springdatajpa.repository.MonHocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/monhoc")
public class MonHocController {

    @Autowired
    private MonHocRepository monHocRepository;

    @GetMapping("/search")
    public String search(Model model){
        List<MonHoc> monHocs = monHocRepository.findAll();
        model.addAttribute("monHocList", monHocs);
        return "monhoc/searchMonHoc";
    }

    @GetMapping("/create")
    public String create(){
        return "monhoc/createMonHoc";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute MonHoc monHoc){
        monHocRepository.save(monHoc);
        return "redirect:/monhoc/search";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") int id){
        monHocRepository.deleteById(id);
        return "redirect:/monhoc/search";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam("id") int id){
        MonHoc monHoc = monHocRepository.findById(id).orElse(null);
        model.addAttribute("monHoc", monHoc);
        return "monhoc/editMonHoc";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("monHoc") MonHoc monHoc){
        monHocRepository.save(monHoc);
        return "redirect:/monhoc/search";
    }
}

