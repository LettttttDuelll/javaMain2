package com.example.laptopshop.config;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;


@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity https) throws Exception {
        return https.
                authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/custom-login", "/login/**", "/register", "/oauth2/authorization/**",
                                "/custom-login/oauth2/code/**","/css/**" , "/img/**","/node_modules/**" , "/home/**" , "/assets/**" , "/js/**" ,"/uploads/**" ,"/cart/**","/detailproduct/**" ,"/laptop_img/**").permitAll()
                        .requestMatchers("/api/v1/users").hasAnyRole("ADMIN","MANAGER")
                        .requestMatchers("/api/v1/laptops").hasAnyRole("ADMIN","MANAGER")
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        //.requestMatchers("/api/v1/**").permitAll()
                        .requestMatchers("/dashboard/**").hasAnyRole("ADMIN","MANAGER")
                        .anyRequest().authenticated()

                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("""
                        {
                            "status": 401,
                            "error": "Unauthorized",
                            "message": "Bạn chưa đăng nhập hoặc token không hợp lệ!"
                        }
                    """);
                        })
                        .accessDeniedHandler(apiAccessDeniedHandler())
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
    @Bean
    public AccessDeniedHandler apiAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("""
            {
                "status": 403,
                "error": "Forbidden",
                "message": "Bạn không có quyền thực hiện chức năng này!"
            }
        """);
        };
    }
}
