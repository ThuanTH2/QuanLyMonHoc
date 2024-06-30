package com.example.springdatajpa.entity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;


import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "Account")
@Data
@ToString(exclude = {"sinhVien", "giangVien"})
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaAccount")
    private Integer maAccount;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "Password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "VaiTro", nullable = false)
    private VaiTro vaiTro;

    public enum VaiTro {
        STUDENT, LECTURER, ADMIN
    }
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, optional = true)
    private SinhVien sinhVien;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, optional = true)
    private GiangVien giangVien;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + vaiTro.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    // Implement other UserDetails methods as needed
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
