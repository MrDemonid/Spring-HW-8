package mr.demonid.spring.hw8.repository;

import mr.demonid.spring.hw8.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByName(String name);

    boolean existsRoleByName(String name);
}
