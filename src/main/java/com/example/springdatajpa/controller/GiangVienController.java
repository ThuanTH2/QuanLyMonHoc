package com.example.springdatajpa.controller;

import com.example.springdatajpa.entity.Account;
import com.example.springdatajpa.entity.GiangVien;
import com.example.springdatajpa.repository.AccountRepository;
import com.example.springdatajpa.repository.GiangVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/giangvien")
public class GiangVienController {

    @Autowired
    private GiangVienRepository giangVienRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/search")
    public String search(Model model) {
        List<GiangVien> giangViens = giangVienRepository.findAll();
        model.addAttribute("giangVienList", giangViens);
        return "giangvien/searchGiangVien";
    }

    @GetMapping("/create")
    public String create() {

        return "giangvien/createGiangVien";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("giangVien") GiangVien giangVien, @ModelAttribute("account") Account account,Model model) {
        // Lấy thông tin từ form và thiết lập cho đối tượng Account
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        try {
            // Lưu đối tượng Account vào cơ sở dữ liệu
            accountRepository.save(account);

            // Thiết lập tài khoản cho giảng viên và lưu giảng viên vào cơ sở dữ liệu
            giangVien.setAccount(account);
            giangVienRepository.save(giangVien);

            return "redirect:/giangvien/search";
        } catch (DataIntegrityViolationException e) {
            // Xử lý khi email đã tồn tại
            model.addAttribute("error", "Email đã tồn tại trong hệ thống.");
            return "giangvien/createGiangVien"; // Điều hướng đến trang thông báo lỗi
        }
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        GiangVien giangVien = giangVienRepository.findById(id).orElse(null);
        if (giangVien != null) {
            giangVienRepository.deleteById(id);
            accountRepository.deleteById(giangVien.getAccount().getMaAccount());
        }
        return "redirect:/giangvien/search";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam("id") int id) {
        GiangVien giangVien = giangVienRepository.findById(id).orElse(null);
        if (giangVien != null) {
            model.addAttribute("giangVien", giangVien);
            model.addAttribute("account", giangVien.getAccount());
            return "giangvien/editGiangVien";
        }
        return "redirect:/giangvien/search";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("giangVien") GiangVien giangVien,Model model) {
        Account account=giangVien.getAccount();
        Account existingAccount = accountRepository.findByEmail(account.getEmail());
        existingAccount.setPassword(passwordEncoder.encode(account.getPassword()));
//        existingAccount.setEmail(account.getEmail()); // Cập nhật email nếu cần thiết
//        existingAccount.setVaiTro(account.getVaiTro());
        try {
            accountRepository.save(existingAccount);
            giangVien.setAccount(existingAccount);
            giangVienRepository.save(giangVien);
            return "redirect:/giangvien/search";
        } catch (DataIntegrityViolationException e) {
            // Xử lý khi email đã tồn tại
            model.addAttribute("error", "Email đã tồn tại trong hệ thống.");
            return "giangvien/editGiangVien"; // Điều hướng đến trang thông báo lỗi
        }

    }
}
