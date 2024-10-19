package mr.demonid.hw7.controller;

import mr.demonid.hw7.domain.User;
import mr.demonid.hw7.dto.RegistrationRequest;
import mr.demonid.hw7.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Collection;

/**
 * В данному случае контроллер общий для всех, поскольку функций очень мало
 * и так удобнее их посмотреть.
 * В реале конечно лучше разнести функционал по специализированным контроллерам.
 */
@Controller
public class BaseController {

    @Autowired
    private UserService userService;

    /**
     * Главная страница.
     */
    @GetMapping
    public String home(Model model, Principal principal) {
        System.out.println("GET: /");
        try
        {
            Path path = Paths.get(getClass().getClassLoader().getResource("static/read.me").toURI());
            System.out.println(path);
            String text = Files.readString(path, StandardCharsets.UTF_8);
            model.addAttribute("fileContent", text);
        } catch (Exception e) {
            model.addAttribute("fileContent", "К сожалению файл не найден!");
        }
        return "/page-index";
    }

    @GetMapping("/index")
    public String index() {
        return "redirect:/";
    }

    /**
     * Страница для всех аутентифицированных пользователей.
     */
    @GetMapping("/public/data")
    public String userPage(Principal principal, Model model, Authentication authentication) {
        System.out.println("PUBLIC");
        if (principal != null) {
            User userDetails = (User) authentication.getPrincipal();

            // Получение данных пользователя
            String username = userDetails.getUsername();
            String email = userDetails.getEmail();

            // Получение ролей пользователя
            Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

            // Передача данных в модель
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("roles", roles);
        }
        return "/page-public";
    }

    /**
     * Страница для админов и разработчиков.
     */
    @GetMapping("/private/data")
    public  String adminPage(Principal principal, Model model, Authentication authentication) {
        System.out.println("PRIVATE");
        if (principal != null) {
            User userDetails = (User) authentication.getPrincipal();

            // Получение данных пользователя
            String username = userDetails.getUsername();
            String email = userDetails.getEmail();

            // Получение ролей пользователя
            Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

            // Передача данных в модель
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("roles", roles);
        }
        return "/page-public";
    }


    /**
     * Форма регистрации нового пользователя.
     * @return
     */
    @GetMapping("/register")
    public String registerUser(Model model) {
        model.addAttribute("user", new RegistrationRequest());
        return "/page-register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute("user") RegistrationRequest user, BindingResult result, Model model) {
        // проверяем пароли
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "100500", "Пароли не совпадают!");
        }
        if (result.hasErrors()) {
            return "/page-register";    // были ошибки, повторяем.
        }
        try {
            if (userService.registerUser(user) != null) {
                return "redirect:/login";
            }
            throw new Exception("Непредвиденная ошибка, попробуйте повторить!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка!");
            model.addAttribute("errorDetails", e.getMessage());
            return "/error";
        }
    }


    /**
     * Форма входа зарегистрированных пользователей.
     * @return
     */
    @GetMapping("/login")
    public String loginUser(Model model) {
        return "page-login";
    }

    /**
     * Страница предупреждения о нехватке прав пользователя.
     * @return
     */
    @GetMapping("/access-denied")
    public String accessDenied(Model model)
    {
        model.addAttribute("errorMessage", "Ошибка доступа");
        model.addAttribute("errorDetails", "Недостаточно прав для доступа к запрашиваемому ресурсу!");
        return "/error";
    }
}
