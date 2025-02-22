package com.example.GrimMadang.config;

import com.example.GrimMadang.security.CustomAuthenticationEntryPoint;
import com.example.GrimMadang.security.costomfilter.*;
import com.example.GrimMadang.shared.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.cors.CorsConfiguration;
import java.util.Collections;
import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${CORS_DOMAIN}")
    private String cors_domain;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomOauthFailureHandler customOauthFailureHandler() {
        return new CustomOauthFailureHandler();
    }

    @Autowired
    private CustomOidcLogoutSuccessHandler customOidcLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationConfiguration authenticationConfiguration,
            JwtTokenProvider jwtTokenProvider,
            UserDetailsService userDetailsService,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {

        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        LoginFilter elderLoginFilter = new LoginFilter(authenticationManager, jwtTokenProvider);
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider, userDetailsService);
        CustomOauthSuccessHandler customOauthSuccessHandler = new CustomOauthSuccessHandler(userDetailsService, jwtTokenProvider);

        http
                .securityMatcher("/**")
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않음
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화

                //배포시 주석 처리 -> 시큐리티,웹 콘피그,프로퍼타이즈 주석 처리
                // .cors(cors -> cors.configurationSource(request -> {
                //     CorsConfiguration config = new CorsConfiguration();
                //     config.setAllowedOrigins(Collections.singletonList("http://localhost:4173"));
                //     config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                //     config.setAllowedHeaders(Arrays.asList("*"));
                //     config.setAllowCredentials(true);  // credentials 허용
                //     return config;
                // }))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/users/register/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/login**", "/error**").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessHandler(customOidcLogoutSuccessHandler)
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID", "jwt")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(customOauthSuccessHandler)  // 로그인 성공 시 처리
                        .failureHandler(customOauthFailureHandler())  // 로그인 실패 시 처리
                        .permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // 인증 실패 처리
                );

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(elderLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
