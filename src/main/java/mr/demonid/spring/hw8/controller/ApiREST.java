package mr.demonid.spring.hw8.controller;

import lombok.AllArgsConstructor;
import mr.demonid.spring.hw8.domain.User;
import mr.demonid.spring.hw8.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiREST {

    private UserService userService;

    @GetMapping("get-users")
    public String getUsers() {
        try {
            List<User> users = userService.getAllUsersWithLimit(0, 12);
            return users.toString();

        } catch (Exception e) {
            return "Api error: can't sql-request: " + e.getMessage();
        }
    }
}
