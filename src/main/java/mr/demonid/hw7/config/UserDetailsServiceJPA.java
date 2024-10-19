package mr.demonid.hw7.config;

import mr.demonid.hw7.domain.Role;
import mr.demonid.hw7.domain.User;
import mr.demonid.hw7.dto.RegistrationRequest;
import mr.demonid.hw7.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Заменяем интерфейс UserDetailsService, поскольку мы включили БД.
 * Через него Spring Security получает аутентифицированный объект
 * пользователя (UserDetails).
 */
@Service
public class UserDetailsServiceJPA implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceJPA(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Пользователь '%s' не найден!", username)));
        return user;
    }
}
