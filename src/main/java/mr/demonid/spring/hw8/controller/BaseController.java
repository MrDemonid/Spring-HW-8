package mr.demonid.spring.hw8.controller;

import lombok.AllArgsConstructor;
import mr.demonid.spring.hw8.aspect.TrackUserAction;
import mr.demonid.spring.hw8.controller.CommonMethods;
import mr.demonid.spring.hw8.domain.Role;
import mr.demonid.spring.hw8.domain.User;
import mr.demonid.spring.hw8.dto.UserInfoRequest;
import mr.demonid.spring.hw8.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;


@Controller
@AllArgsConstructor
public class BaseController {

    private final UserService userService;
    CommonMethods commonMethods;

    /**
     * Главная страница.
     */
    @TrackUserAction
    @GetMapping
    public String home(Model model, Principal principal) {
        try
        {
            Path path = Paths.get(getClass().getClassLoader().getResource("static/read.me").toURI());
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
     * Личная страничка аутентифицированных пользователей.
     */
    @GetMapping("/profile")
    public String userPage(Principal principal, Model model, Authentication authentication) {
        try {
            // получаем актуальные данные о текущем пользователе
            User user = userService.getUserById(((User) authentication.getPrincipal()).getId());
            UserInfoRequest userInfo = new UserInfoRequest(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getAccountsNameList(),
                    user.getAccountsAmountList(),
                    user.getRoles().stream().map(Role::getName).toList().toString()
            );
            // Передача данных в модель
            model.addAttribute("user", userInfo);
            return "/page-user";

        } catch (Exception e) {
            return commonMethods.gotoErrorPage(model, "Ошибка!", "Ошибка получения данных о пользователе!");
        }
    }

    /**
     * Страница для админов и разработчиков.
     */
    @GetMapping("/private/data")
    public  String adminPage(Principal principal, Model model, Authentication authentication) {
        try {
            List<User> users = userService.getUsersWithRole("ROLE_USER");
            List<UserInfoRequest> list = users.stream().map(e -> new UserInfoRequest(
                    e.getId(),
                    e.getUsername(),
                    e.getEmail(),
                    e.getAccountsNameList(),
                    e.getAccountsAmountList(),
                    e.getRolesList())
            ).toList();
            model.addAttribute("users", list);
        } catch (Exception e) {
            return commonMethods.gotoErrorPage(model, "Ошибка", e.getMessage());
        }
        return "/page-private";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/private/data";
    }
}
