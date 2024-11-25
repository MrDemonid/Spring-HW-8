package mr.demonid.spring.hw8.controller;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * Страница с ошибкой.
 */
@Service
public class CommonMethods {

    public String gotoErrorPage(Model model, String message, String details) {
        model.addAttribute("errorMessage", message);
        model.addAttribute("errorDetails", details);
        return "/error";
    }

}
