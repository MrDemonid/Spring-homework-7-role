package mr.demonid.hw7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Настройка цепочки фильтров блока аутентификации.
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(e -> e
                        .requestMatchers("/", "/index", "/login-page", "/login", "/register", "/css/**").permitAll()
                        .requestMatchers("/private/**").hasAnyRole("ADMIN", "DEVELOPER")
                        .requestMatchers("/public/**").authenticated()
                        .anyRequest().authenticated())

                .exceptionHandling((h) -> h.accessDeniedPage("/access-denied"))

                .formLogin(login -> login
                        .loginPage("/login")                    // адрес страницы для входа
                        .defaultSuccessUrl("/", true)           // куда перейдем при успешной авторизации
                        .permitAll())
                .logout(form -> form
                        .logoutSuccessUrl("/"));                // сюда перейдем после выхода

        return http.build();
    }

    /**
     * Переопределяем PasswordEncoder
     * Ставим дефолтный (шифрование Base64)
     * Всё это только из-за того, что PasswordEncoder должен переопределяться
     * с UserDetailsService ы паре.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Переопределяем UserDetailsService, поскольку будем использовать свою БД.
     * Прим: Переопределение реализовано как отдельный класс (UserService)
     */
}
