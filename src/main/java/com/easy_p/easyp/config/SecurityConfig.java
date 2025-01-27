package com.easy_p.easyp.config;

import com.easy_p.easyp.common.jwt.JwtProvider;
import com.easy_p.easyp.config.filter.JwtValidFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기본 login form 비활성화
                .logout(AbstractHttpConfigurer::disable) // 기본 logout 비활성화
                .authorizeHttpRequests((request) -> request
                        .requestMatchers(HttpMethod.POST,"/member/oauth2/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtValidFilter(jwtProvider, userDetailsService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://localhost:5173","http://localhost:5173","https://devmate-fe.vercel.app","https://localhost:3000")
                .allowedMethods("*")
                .allowCredentials(true);
    }
}
