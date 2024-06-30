package com.example.springdatajpa.controller;

import com.example.springdatajpa.entity.Account;
import com.example.springdatajpa.repository.AccountRepository;
import com.example.springdatajpa.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @GetMapping("/list")
    public String listAccounts(Model model) {
        List<Account> accounts = accountRepository.findAll();
        model.addAttribute("accounts", accounts);
        return "account/list"; // Trả về template list.html để hiển thị danh sách account
    }
    @GetMapping("/create")
    public String create() {
        return "account/create";
    }
    @PostMapping("/create")
    public String create(@ModelAttribute Account account) {
        accountService.saveAccount(account); // Sử dụng AccountService để mã hóa mật khẩu và lưu tài khoản
        return "/login";
    }

}

