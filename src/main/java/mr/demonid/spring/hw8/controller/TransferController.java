package mr.demonid.spring.hw8.controller;

import lombok.AllArgsConstructor;
import mr.demonid.spring.hw8.domain.User;
import mr.demonid.spring.hw8.dto.TransferRequest;
import mr.demonid.spring.hw8.dto.UserInfoRequest;
import mr.demonid.spring.hw8.services.TransferService;
import mr.demonid.spring.hw8.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

/**
 * Контроллер трансфера средств пользователей.
 */
@AllArgsConstructor
@Controller
public class TransferController {

    private UserService userService;
    private TransferService transferService;
    private CommonMethods commonMethods;


    @PostMapping("/begin-transfer")
    public String setupTransfer(@ModelAttribute("user") UserInfoRequest userInfo, Model model) {
        try {
            List<User> users = userService.getUsersWithRole("USER");
            model.addAttribute("user", userInfo);
            model.addAttribute("recipients", users.stream().filter(e -> !Objects.equals(e.getId(), userInfo.getId())).toList());
            model.addAttribute("payment", new TransferRequest());
            return "/page-transfer";

        } catch (Exception e) {
            return commonMethods.gotoErrorPage(model, "Ошибка!", e.getMessage());
        }
    }

    @PostMapping(path = "/transfer")    // , consumes = "application/x-www-form-urlencoded")
    public String transfer(@RequestBody TransferRequest request, Model model, Authentication authentication) {
        try {
            transferService.transfer(request);
            User userDetails = (User) authentication.getPrincipal();
            return "/page-transaction-done";

        } catch (Exception e) {
            return commonMethods.gotoErrorPage(model, "Ошибка!", e.getMessage());
        }
    }

    /**
     * Если попали сюда, то перевод выполнен успешно.
     */
    @GetMapping("/transfer")
    public String getTransfer() {

        return "/page-transaction-done";
    }

}
