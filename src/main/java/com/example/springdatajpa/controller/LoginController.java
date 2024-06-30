package com.example.springdatajpa.controller;

import com.example.springdatajpa.entity.Account;
import com.example.springdatajpa.repository.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                return "redirect:/home/admin";
            } else if (role.equals("ROLE_LECTURER")) {
                return "redirect:/home/lecturer";
            } else {
                return "redirect:/home/student";
            }

        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
    @GetMapping("/home/admin")
    public String adminHome(){
        return "home/admin";
    }

    @GetMapping("/home/lecturer")
    public String lecturerHome(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        model.addAttribute("maUser", account.getGiangVien().getMaGiangVien());
        model.addAttribute("tenUser", account.getGiangVien().getTenGiangVien());
        return "home/lecturer";
    }

    @GetMapping("/home/student")
    public String studenHome(Model model
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        model.addAttribute("maUser", account.getSinhVien().getMaSinhVien());
        model.addAttribute("tenUser", account.getSinhVien().getTenSinhVien());
        return "home/student";
    }


//    @PostMapping("/login")
//    public String login(Account account, Model model) {
//        Account existingAccount = accountRepository.findByEmail(account.getEmail());
//
//        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
//            // Log successful login attempt
//            model.addAttribute("account", existingAccount);
//            return "redirect:/giangvien/search";
//        } else {
//            // Log failed login attempt
//            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
//            return "login";
//        }
//
//    }

}