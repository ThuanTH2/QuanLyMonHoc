package com.example.springdatajpa.security;

import com.example.springdatajpa.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/login","/diemthi/list").permitAll()
                .requestMatchers("/monhoc/**").hasRole("ADMIN")
                .requestMatchers("/account/**").hasRole("ADMIN")
                .requestMatchers("/giangvien/**").hasRole("ADMIN")
                .requestMatchers("/sinhvien/**").hasRole("ADMIN")
                .requestMatchers("/lophoc/**").hasRole("ADMIN")
                .requestMatchers("/lophocsinhvien/**").hasRole("ADMIN")
                .requestMatchers("/diemthi/khoa").hasRole("ADMIN")
                .requestMatchers("/diemthi/Security").hasRole("ADMIN")
                .requestMatchers("/diemthi/khoa/{maLopHoc}").hasRole("ADMIN")
                .requestMatchers("/diemthi/mokhoa/{maLopHoc}").hasRole("ADMIN")
                .requestMatchers("/diemthi/list").hasAnyRole("LECTURER","ADMIN","STUDENT")
                .requestMatchers("/diemthi//list/{maLopHoc}").hasAnyRole("LECTURER","ADMIN")
                .requestMatchers("diemthi/nhapdiem/{maLopHoc}").hasAnyRole("LECTURER","ADMIN")
                .requestMatchers("diemthi/edit").hasAnyRole("LECTURER","ADMIN")
                .requestMatchers("diemthi/student").hasRole("STUDENT")
                .requestMatchers("home/student").hasRole("STUDENT")
                .requestMatchers("home/admin").hasRole("ADMIN")
                .requestMatchers("home/lecturer").hasRole("LECTURER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
//                .defaultSuccessUrl("/home", true) // Chuyển hướng đến /default sau khi đăng nhập thành công
                .successHandler((request, response, authentication) -> {
                    String role = authentication.getAuthorities().iterator().next().getAuthority();
                    if (role.equals("ROLE_ADMIN")) {
                        response.sendRedirect("/home/admin");
                    } else if (role.equals("ROLE_LECTURER")) {
                        response.sendRedirect("/home/lecturer");
                    } else {
                        response.sendRedirect("/home/student");
                    }
                })
                .failureUrl("/login?error=true") // Chuyển hướng lại trang đăng nhập với thông báo lỗi nếu đăng nhập thất bại
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout") // Chuyển hướng lại trang đăng nhập với thông báo logout thành công
                .permitAll();

        return http.build();
    }
}