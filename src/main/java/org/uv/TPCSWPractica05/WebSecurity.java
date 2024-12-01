package org.uv.TPCSWPractica05;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final JwtUtil jwtUtil;

    public WebSecurity(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/auth/**").permitAll() // Permite el acceso a rutas de autenticación
                .antMatchers("/departamentos/**").authenticated() // Solo usuarios autenticados pueden acceder a rutas de departamentos
                .antMatchers("/empleados/**").authenticated() // Solo usuarios autenticados pueden acceder a rutas de empleados
                .anyRequest().authenticated() // Requiere autenticación para todas las demás rutas
            .and()
            .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class); // Filtro JWT antes de la autenticación básica

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Codificador de contraseñas
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Administrador de autenticación
    }
}
