package mr.demonid.hw7.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Класс для запроса на регистрацию нового пользователя.
 */
@Data
@AllArgsConstructor
public class RegistrationRequest implements Serializable
{
    private String username;
    private String password;
    private String email;
}
