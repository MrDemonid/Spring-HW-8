package mr.demonid.spring.hw8.services;

import lombok.AllArgsConstructor;
import mr.demonid.spring.hw8.aspect.TrackUserAction;
import mr.demonid.spring.hw8.domain.Role;
import mr.demonid.spring.hw8.domain.User;
import mr.demonid.spring.hw8.dto.RegistrationRequest;
import mr.demonid.spring.hw8.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    private AccountService accountService;
    private RoleService roleService;


    /**
     * Регистрация нового пользователя.
     * Сразу добавляем ему счёт.
     * @param reques Данные о новом пользователе.
     */
    @TrackUserAction
    @Transactional
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
        user.addRole(roleService.getRole("USER"));
        if (reques.getUsername().equalsIgnoreCase("admin")) {
            // пасхалка для хакеров :)
            user.addRole(roleService.getRole("ADMIN"));
        }
        user.addAccount(accountService.createNewAccount());
        return userRepository.save(user);
    }

    /**
     * Возвращает данные о пользователе из БД, со всеми зависимостями.
     */
    @TrackUserAction
    public User getUserById(Long id) throws Exception {
        Optional<User> userOptional = userRepository.findUserById(id);
        if (userOptional.isEmpty()) {
            throw new Exception("Пользователь с ID = " + id + " не найден!");
        }
        return userOptional.get();
    }

    /**
     * Возвращает список всех пользователей "USER".
     * Просто практикуюсь на stream :)
     */
    @TrackUserAction
    public List<User> getUsersWithRole(String roleName) throws Exception {
        Role role = roleService.getRole(roleName);
        List<User> all = userRepository.findAllUsers();
        return all.stream().filter(e -> e.getRoles().contains(role)).toList();
    }


    /**
     * Возвращает список пользователей, выдавая его постранично.
     */
    @TrackUserAction
    public List<User> getAllUsersWithLimit(int page, int pageSize) throws Exception {
        List<User> users = userRepository.findAllUsersWithLimit(PageRequest.of(page, pageSize));
        if (users == null) {
            throw new Exception("База данных пользователей пуста.");
        }
        return users;
    }

    @TrackUserAction
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
