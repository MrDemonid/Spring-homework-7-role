package mr.demonid.hw7.repository;

import mr.demonid.hw7.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Возвращает данные пользователя.
     * @param username Имя пользователя.
     */
    Optional<User> findByUsername(String username);

    /**
     * Проверяет наличие пользователя в БД по его имени.
     * @param username Имя пользователя.
     */
    boolean existsByUsername(String username);

    boolean existsUserByEmail(String email);

    /**
     * Возвращает данные пользователя, идентифицируя его по имени или по почте.
     * @param username Имя пользователя (уникальное)
     * @param email    Его почта (тоже уникальная).
     */
    Optional<User> findUserByUsernameOrEmail(String username, String email);
}

