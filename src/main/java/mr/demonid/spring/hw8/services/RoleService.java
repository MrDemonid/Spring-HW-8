package mr.demonid.spring.hw8.services;

import lombok.AllArgsConstructor;
import mr.demonid.spring.hw8.domain.Role;
import mr.demonid.spring.hw8.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RoleService {

    private RoleRepository roleRepository;

    public void createRoles(String ... names) {
        Arrays.stream(names).forEach(name -> {
            if (!name.startsWith("ROLE_")) {
                name = "ROLE_" + name;
            }
            if (!roleRepository.existsRoleByName(name)) {
                Role role = new Role();
                role.setName(name);
                role.setDescription("");
                role = roleRepository.save(role);
                System.out.println("-- add role '" + role.getName() + "'");
            }
        });
    }

    public Role getRole(String name) throws Exception {
        if (!name.startsWith("ROLE_")) {
            name = "ROLE_" + name;
        }
        Optional<Role> role = roleRepository.findRoleByName(name);
        if (role.isEmpty()) {
            throw new Exception("Такой роли не существует!");
        }
        return role.get();
    }

}
