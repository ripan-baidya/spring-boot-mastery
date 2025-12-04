package in.ripanbaidya.openapidoc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF in development
            .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll() // Allow all endpoints
            )
            .formLogin(AbstractHttpConfigurer::disable) // Disable login form
            .httpBasic(AbstractHttpConfigurer::disable); // Disable basic auth popup

        return http.build();
    }
}
