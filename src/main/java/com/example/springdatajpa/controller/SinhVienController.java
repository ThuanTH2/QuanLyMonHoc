package com.example.springdatajpa.controller;

import com.example.springdatajpa.entity.Account;
import com.example.springdatajpa.entity.SinhVien;
import com.example.springdatajpa.repository.AccountRepository;
import com.example.springdatajpa.repository.SinhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sinhvien")
public class SinhVienController {

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/search")
    public String search(Model model) {
        List<SinhVien> sinhViens = sinhVienRepository.findAll();
        model.addAttribute("sinhVienList", sinhViens);
        return "sinhvien/searchSinhVien";
    }

    @GetMapping("/create")
    public String create() {
        return "sinhvien/createSinhVien";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("sinhVien") SinhVien sinhVien, @ModelAttribute("account") Account account, Model model) {
        // Lấy thông tin từ form và thiết lập cho đối tượng Account
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        try {
            // Lưu đối tượng Account vào cơ sở dữ liệu
            accountRepository.save(account);

            // Thiết lập tài khoản cho sinh viên và lưu sinh viên vào cơ sở dữ liệu
            sinhVien.setAccount(account);
            sinhVienRepository.save(sinhVien);

            return "redirect:/sinhvien/search";
        } catch (DataIntegrityViolationException e) {
            // Xử lý khi email đã tồn tại
            model.addAttribute("error", "Email đã tồn tại trong hệ thống.");
            return "sinhvien/createSinhVien"; // Điều hướng đến trang thông báo lỗi
        }
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        SinhVien sinhVien = sinhVienRepository.findById(id).orElse(null);
        if (sinhVien != null) {
            sinhVienRepository.deleteById(id);
            accountRepository.deleteById(sinhVien.getAccount().getMaAccount());
        }
        return "redirect:/sinhvien/search";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam("id") int id) {
        SinhVien sinhVien = sinhVienRepository.findById(id).orElse(null);
        if (sinhVien != null) {
            model.addAttribute("sinhVien", sinhVien);
            model.addAttribute("account", sinhVien.getAccount());
            return "sinhvien/editSinhVien";
        }
        return "redirect:/sinhvien/search";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("sinhVien") SinhVien sinhVien, Model model) {
        Account account = sinhVien.getAccount();
        Account existingAccount = accountRepository.findByEmail(account.getEmail());
        existingAccount.setPassword(passwordEncoder.encode(account.getPassword()));
        existingAccount.setEmail(account.getEmail()); // Cập nhật email nếu cần thiết
        existingAccount.setVaiTro(account.getVaiTro());
        try {
            accountRepository.save(existingAccount);
            sinhVien.setAccount(existingAccount);
            sinhVienRepository.save(sinhVien);
            return "redirect:/sinhvien/search";
        } catch (DataIntegrityViolationException e) {
            // Xử lý khi email đã tồn tại
            model.addAttribute("error", "Email đã tồn tại trong hệ thống.");
            return "sinhvien/editSinhVien"; // Điều hướng đến trang thông báo lỗi
        }
    }
}
