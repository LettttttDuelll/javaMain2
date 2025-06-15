package com.example.laptopshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity https) throws Exception {
        return https.
                authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/custom-login", "/login/**", "/register", "/oauth2/authorization/**",
                                "/custom-login/oauth2/code/**","/css/**" , "/img/**","/node_modules/**" , "/home/**" , "/assets/**" , "/js/**" ,"/uploads/**" ,"/cart/**","/detailproduct/**" ,"/laptop_img/**").permitAll()
                        .requestMatchers("/api/v1/**").permitAll()
                        .requestMatchers("/dashboard/**").hasRole("ADMIN")
                        .anyRequest().authenticated()

                )
                //.formLogin(form -> form
                 //       .loginPage("/custom-login") // nếu có login riêng
                     //   .permitAll()
                //)
                //.oauth2Login(oauth2 -> oauth2
                        //.successHandler(customSuccessHandler)
                //)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
