package com.example.caliberclothing.config;

import com.example.caliberclothing.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Lazy
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow specific origins (more secure than allowing all)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",  // React development server
                "http://127.0.0.1:3000"   // Alternative localhost
        ));

        // Allow all HTTP methods needed by your React app
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"
        ));

        // Allow all headers
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Allow credentials (important for authentication)
        configuration.setAllowCredentials(true);

        // Cache preflight response for 1 hour
        configuration.setMaxAge(3600L);

        // Expose common headers to the frontend
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization", "Content-Type", "Accept", "Origin",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                // Enable Basic Auth
                .httpBasic(Customizer.withDefaults())

                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**", "/api/public/**", "/api/products/**",
                                "/api/categories/**", "/api/health/**", "/api/statuses/**",
                                "/api/provinces/**", "/api/debug/**", "/uploads/**",
                                "/api/customer/register").permitAll()
                        .requestMatchers("/api/ceo/**", "/api/reports/**", "/api/upload/**").hasRole("CEO")
                        .requestMatchers("/api/product-manager/**").hasRole("PRODUCT_MANAGER")
                        .requestMatchers("/api/merchandise-manager/**", "/api/notifications/**").hasRole("MERCHANDISE_MANAGER")
                        .requestMatchers("/api/dispatch-officer/**").hasRole("DISPATCH_OFFICER")
                        .requestMatchers("/api/customer/**", "/api/cart/**", "/api/wishlist/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/employees/**").hasRole("CEO")
                        .requestMatchers("/api/admin/**").hasRole("CEO")
                        .requestMatchers("/api/employee/**").hasAnyRole("CEO", "PRODUCT_MANAGER", "MERCHANDISE_MANAGER", "DISPATCH_OFFICER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .authenticationProvider(authenticationProvider());

        return http.build();
    }
}