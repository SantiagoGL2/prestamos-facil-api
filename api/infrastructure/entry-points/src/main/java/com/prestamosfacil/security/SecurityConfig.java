package com.prestamosfacil.security;

import com.prestamosfacil.constant.ConfiguracionSeguridadConstantes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(ConfiguracionSeguridadConstantes.FUERZA_HASH_PASSWORD);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/tipos-documento/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/tipos-prestamo/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/solicitudes-prestamo").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/solicitudes-prestamo/**").hasRole("ANALISTA")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/solicitudes-prestamo/**").hasRole("ANALISTA")
                        .requestMatchers(HttpMethod.POST, "/api/v1/analistas").hasRole("ANALISTA")
                        .requestMatchers(HttpMethod.GET, "/api/v1/analistas").hasRole("ANALISTA")
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/**").hasRole("ANALISTA")
                        .requestMatchers("/api/v1/reportes/**").hasRole("ANALISTA")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
