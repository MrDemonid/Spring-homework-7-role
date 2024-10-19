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

    /**
     * Проверяет наличие эл. почты в БД.
     */
    Boolean existsByEmail(String email);

}

