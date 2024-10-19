package mr.demonid.hw7.services;

import lombok.AllArgsConstructor;
import mr.demonid.hw7.domain.Role;
import mr.demonid.hw7.domain.User;
import mr.demonid.hw7.dto.RegistrationRequest;
import mr.demonid.hw7.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegistrationRequest reques) throws Exception {
        if (reques.getUsername().isBlank() || reques.getPassword().isBlank() || reques.getEmail().isBlank()) {
            throw new Exception("Данные пользователя неверны!");
        }
        if (userRepository.existsByUsername(reques.getUsername())) {
            throw new Exception("Такой пользователь уже существует!");
        }
        if (userRepository.existsUserByEmail(reques.getEmail())) {
            throw new Exception("Почта уже используется!");
        }
        User user = new User();
        user.setUsername(reques.getUsername());
        user.setPassword(passwordEncoder.encode(reques.getPassword()));
        user.setEmail(reques.getEmail());
        user.setRole(Role.ROLE_USER);
        return userRepository.save(user);
    }
}
