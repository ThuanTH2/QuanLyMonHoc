package com.example.springdatajpa.service;

import com.example.springdatajpa.entity.Account;
import com.example.springdatajpa.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);

        if (account == null) {
            throw new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email);
        }

        return account;
    }
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void saveAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
    }
    public Account.VaiTro getCurrentUserRole(Authentication authentication) {
            GrantedAuthority authority = authentication.getAuthorities().iterator().next();
            return Account.VaiTro.valueOf(authority.getAuthority().replace("ROLE_", ""));
    }
}
