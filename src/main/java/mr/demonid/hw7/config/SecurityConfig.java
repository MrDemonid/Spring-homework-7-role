package mr.demonid.hw7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                        .requestMatchers("/", "/index", "/css/**", "/*.ico").permitAll()
                        .requestMatchers("/public/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/private/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .defaultSuccessUrl("/")                 // куда перейдем при успешной авторизации
                        .permitAll())                           // доступна всем
                .logout(form -> form.logoutSuccessUrl("/"));    // сюда перейдем после выхода
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
