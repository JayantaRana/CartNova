package com.example.jwtDemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.jwtDemo.filter.JwtAuthenticationFilter;
import com.example.jwtDemo.service.CustomUserDetailsService;

//new add
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    // new add
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            AuthenticationProvider authenticationProvider) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/index.html",
                                "/signup.html", "/login.html", "/admin-login.html",
                                "/customer-home.html", "/view-cart.html",
                                "/admin-home.html",
                                "/add-product.html", "/all-products-admin.html",
                                "/edit-product.html", "/view-product-admin.html",
                                "/css/**", "/js/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/admin/products/**").permitAll() // new add
                        .requestMatchers(HttpMethod.GET, "/auth/products").permitAll()
                        .requestMatchers("/auth/**", "/hello").permitAll()
                        .requestMatchers("/customer/cart/**").hasRole("USER")
                        .requestMatchers("/customer/payment/**").hasRole("USER")
                        .requestMatchers("/products/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/customer/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())

                // .authorizeHttpRequests(auth -> auth
                // // 1. ALLOW PUBLIC ACCESS (No login needed)
                // .requestMatchers("/", "/index.html", "/css/**", "/js/**").permitAll()
                // .requestMatchers("/auth/**", "/hello").permitAll()

                // // THIS LINE IS THE FIX: Allow anyone to VIEW products
                // .requestMatchers(HttpMethod.GET, "/admin/products/**").permitAll()
                // .requestMatchers(HttpMethod.GET, "/products/**").permitAll()

                // // 2. RESTRICTED ACCESS (Login required)
                // // Only Admins can POST, PUT, or DELETE in the admin path
                // .requestMatchers("/admin/**").hasRole("ADMIN")

                // // Users or Admins can access general product actions (like adding to cart)
                // .requestMatchers("/products/**").hasAnyRole("USER", "ADMIN")

                // .requestMatchers("/customer/cart/**").hasRole("USER")
                // .anyRequest().authenticated())

                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}