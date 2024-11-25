package mr.demonid.spring.hw8.config;

import mr.demonid.spring.hw8.domain.Role;
import mr.demonid.spring.hw8.domain.User;
import mr.demonid.spring.hw8.dto.RegistrationRequest;
import mr.demonid.spring.hw8.repository.UserRepository;
import mr.demonid.spring.hw8.services.RoleService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Заменяем интерфейс UserDetailsService, поскольку мы включили БД.
 * Через него Spring Security получает аутентифицированный объект
 * пользователя (UserDetails).
 */
@Service
public class UserDetailsServiceJPA implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceJPA(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;

        // в БД обязательно должны быть роли.
        try {
            roleService.createRoles("USER", "ADMIN", "DEVELOPER", "PRODUCER");
        } catch (Exception e) {}

////         просто добавляем двух юзеров для теста
//        makePerson(new RegistrationRequest("admin", "admin", "admin", "admin@admin.com"), Role.ROLE_ADMIN);
//        makePerson(new RegistrationRequest("user", "password", "password", "user@user.com"), Role.ROLE_USER);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Пользователь '%s' не найден!", username)));
        return user;
    }


//    /*
//       Это можно смело убрать
//    */
//    private void makePerson(RegistrationRequest registrationRequest, Role role) {
//        System.out.println("UserDetailsService(): makePerson()" + registrationRequest.toString());
//        try {
//            if (!userRepository.existsByUsername(registrationRequest.getUsername())) {
//                User user = new User();
//                user.setUsername(registrationRequest.getUsername());
//                user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
//                user.setEmail(registrationRequest.getEmail());
//                user.addRole(role);
//                userRepository.save(user);
//                System.out.println("  -- create user: " + user.getUsername());
//            }
//        } catch (Exception e) {
//            System.out.println("ERROR: " + e.getMessage());
//        }
//    }

}
