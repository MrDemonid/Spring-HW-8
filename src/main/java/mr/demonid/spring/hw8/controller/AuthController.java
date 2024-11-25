package mr.demonid.spring.hw8.controller;

import lombok.AllArgsConstructor;
import mr.demonid.spring.hw8.dto.RegistrationRequest;
import mr.demonid.spring.hw8.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class AuthController {

    private UserService userService;
    CommonMethods commonMethods;


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
            return "/page-register";            // были ошибки, повторяем.
        }
        try {
            if (userService.registerUser(user) != null) {
                return "redirect:/login";       // переходим на форму входа
            }
            throw new Exception("Непредвиденная ошибка, попробуйте повторить!");

        } catch (Exception e) {
            return commonMethods.gotoErrorPage(model, "Ошибка!", e.getMessage());
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
        return commonMethods.gotoErrorPage(model, "Ошибка доступа", "Недостаточно прав для доступа к запрашиваемому ресурсу!");
    }

}
